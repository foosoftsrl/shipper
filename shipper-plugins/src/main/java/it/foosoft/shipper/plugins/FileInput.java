package it.foosoft.shipper.plugins;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.Bag;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.InputContext;
import it.foosoft.shipper.api.Param;

public class FileInput implements Input {
	private static final Logger LOG = LoggerFactory.getLogger(FileInput.class);

    public static String antGlobToRegexp(String globExpression) {
    	globExpression = globExpression.replace("**/", "<anydir>");
    	globExpression = globExpression.replace("*", "<anychar>");
    	globExpression = globExpression.replace("<anydir>", "([^/]*/)*");
    	globExpression = globExpression.replace("<anychar>", "[^/]*");
    	return globExpression;
    }
	
    @NotNull
	@Param
	String path;

	@Param
	int threads = 1;
	
	@Param
	int discover_interval = 15;

	private InputContext ctx;

	// the result of the last scan
	private Set<Path> lastScan = new HashSet<>();

	// The files queued for download
	private LinkedBlockingQueue<Entry> taskQueue = new LinkedBlockingQueue<>();

	// executor which discovers new files
	private ScheduledExecutorService scanExecutor;

	private Pattern pattern;

	private Path baseDir;

	List<Worker> workers = new ArrayList<>();
	
	private static class Entry {
		public static Entry STOP = new Entry(null, null);
		final Path path;
		final FileTime lastModified;
		final int startOffset;
		Entry(Path file, FileTime fileTime) {
			this(file, fileTime, 0);
		}
		Entry(Path file, FileTime fileTime, int startOffset) {
			this.path = file;
			this.lastModified = fileTime;
			this.startOffset = startOffset;
		}
	}

	public FileInput(InputContext ctx) {
		this.ctx = ctx;
	}

	class Worker {
		private Thread thread;
		private Entry currentEntry;
		public AtomicBoolean stop = new AtomicBoolean();
		public Worker() {
			thread = new Thread(()->{
				try {
					run();
				} catch (InterruptedException e) {
					LOG.info("Worker exiting on unexpected interrupted exception");
				}
			});
			thread.start();
		}

		public void stop() throws InterruptedException {
			stop.set(true);
			thread.join();
		}

		private void run() throws InterruptedException {
			while(true) {
				var entry = taskQueue.take();
				if(entry == Entry.STOP || stop.get()) {
					return;
				}
				download(entry);
			}
		}

		private void download(Entry entry) {
			currentEntry = entry;
			int currentLine = 0;
			try {
				try(InputStream istr = getInputStream(entry)) {
					try(var reader = new InputStreamReader(istr)) {
						try(var bufferedReader = new BufferedReader(reader)) {
							String line;
							while((line = bufferedReader.readLine()) != null && !stop.get()) {
								if(currentLine < entry.startOffset) {
									LOG.debug("Skipping already processed line");
								} else {
									Event e = ctx.createEvent();
									e.setField("message", line);
									ctx.processEvent(e);
								}
								currentLine++;
							}
							// if we arrived here on a stop request and we re
							if(stop.get()) {
								int restartLine = Math.max(currentLine, entry.startOffset);
								currentEntry = new Entry(currentEntry.path, currentEntry.lastModified, restartLine); 
							} else {
								currentEntry = null; 
								Files.deleteIfExists(entry.path);
								return;
							}
						}
					}
				}
			} catch(Exception e) {
				LOG.error("Failed processing file {}. Leaving it on disk", entry.path, e);
				currentEntry = null; 
			}
		}


		// Create a possibly de-compressing input stream for an entry 
		private InputStream getInputStream(Entry entry) throws IOException {
			FileInputStream istr = new FileInputStream(entry.path.toFile().getAbsolutePath());
			if(entry.path.toFile().getName().endsWith(".gz")) {
				try {
					return new GZIPInputStream(istr);
				} catch(IOException e) {
					istr.close();
					throw e;
				}
			}
			return istr;
		}
	}

	@Override
	public void start() {
		if(path == null) {
			throw new IllegalArgumentException("missing path parameter");
		}
		int pos = path.indexOf("*");
		if(pos < 0) {
			throw new IllegalArgumentException("the path parameter should have a wildcard, or something like that");
		}
		Bag bag = ctx.getStartStatus();
		if(bag != null) {
			Bag progressBag = bag.getBagProperty("progress");
			if(progressBag != null) {
				for(String fileName:progressBag.getPropertyNames()) {
					Long l = progressBag.getNumericProperty(fileName);
					if(l != null) {
						Entry entry = new Entry(Path.of(fileName), null, l.intValue());
						taskQueue.add(entry);
						lastScan.add(entry.path);
					}
				}
			}
		}

		baseDir = Path.of(path.substring(0,pos));
		String filter = path.substring(pos);
		String filterRegex = antGlobToRegexp(filter);
		pattern = Pattern.compile(filterRegex);
		scanExecutor = Executors.newScheduledThreadPool(1);
		scanExecutor.scheduleWithFixedDelay(this::scan, 0, discover_interval * 500, TimeUnit.MILLISECONDS);
		
		Bag startStartus = ctx.getStartStatus();
		if(startStartus != null) {
			Bag progress = bag.getBagProperty("progress");
			if(progress != null) {
				for(String pathStr: progress.getPropertyNames()) {
					Long idx = progress.getNumericProperty(pathStr);
					if(idx != null) {
						Path path = Path.of(pathStr);
						try {
							Entry entry = new Entry(path, Files.getLastModifiedTime(path), idx.intValue());
							lastScan.add(entry.path);
							taskQueue.add(entry);
							LOG.info("Restarting {} @ {}", entry.path, idx);
						} catch (IOException e) {
							LOG.warn("Unprocessable restart entry: {}", path);
						}
					}
				}
			}
		}
		
		for(int i = 0; i < threads; i++) {
			workers.add(new Worker());
		}
	}

	@Override
	public void stop() {
		stop(false);
	}

	void stopAndCompleteQueuedJobs() {
		stop(true);
	}

	/*
	 * Will create a bag such as 
	 * {
	 *   "progress" : {
	 *     "/path/to/file": xxxx
	 *   }
	 * }
	 * 
	 * where xxxx is a line number
	 */
	public Bag computeStopStatus() {
		Bag bag = ctx.createBag();
		Bag progressBag = ctx.createBag();
		bag.setBagProperty("progress", progressBag);
		for(Worker worker: workers) {
			Entry currentEntry = worker.currentEntry;
			if(currentEntry != null) {
				progressBag.setNumericProperty(currentEntry.path.toString(), currentEntry.startOffset);
				LOG.info("Saving line position {} for file {}", currentEntry.startOffset, currentEntry.path);
			}
		}
		return bag;
	}
	// This method is visible to package for unit testing 
	private void stop(boolean completeQueuedTasks) {
		scanExecutor.shutdownNow();
		try {
			if(!scanExecutor.awaitTermination(120, TimeUnit.SECONDS)) {
				LOG.warn("Stopping the scanner task took too long, giving up");
			} else {
				LOG.info("Stopped directory scanner");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Queue an Entry.Stop event for each worker, they will take (at most) one each
		for(int i = 0; i < workers.size(); i++) {
			taskQueue.add(Entry.STOP);
		}

		// Now we can stop the workers, they may be blocked for a while until downStream un-freezes
		for(var worker : workers) {
			try {
				worker.stop();
				LOG.info("Stopped worker");
			} catch (InterruptedException e) {
				LOG.error("Failed stopping worker");
			}
		}
		ctx.setStopStatus(computeStopStatus());
	}

	
	public void scan() {
		try {
		    var entries = new ArrayList<Entry>();
		    recursivelyScanDirectory(baseDir, entry->{
		    	entries.add(entry);
		    });
		    entries.sort(FileInput::compareLastModified);
		    updateTaskList(entries);
		} catch(Exception e) {
			e.printStackTrace();
			LOG.error("Failed getting remote file list {}", e.getMessage());
		}
	}

	private void updateTaskList(ArrayList<Entry> entries) throws InterruptedException {
		Set<Path> fileList = new HashSet<>();
		for(Entry e : entries) {
		    if(!lastScan.contains(e.path)) {
		    	taskQueue.put(e);
		    }
	    	fileList.add(e.path);
		}
		lastScan = fileList;
	}

	private static int compareLastModified(Entry left, Entry right) {
		return left.lastModified.compareTo(right.lastModified);
	}
	
	private void recursivelyScanDirectory(Path dir, Consumer<Entry> consumer) throws IOException {
		LOG.info("Scanning {}", dir);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for(Path file: stream) {
				if(file.getFileName().startsWith("."))
					continue;
				if(Files.isDirectory(file)) {
					 recursivelyScanDirectory(file, consumer);
				}
				else {
					Path relativePath = baseDir.relativize(file);
					String relPathUnix = relativePath.toString().replace("\\", "/");
					if(pattern.matcher(relPathUnix).matches()) {
						consumer.accept(new Entry(file, Files.getLastModifiedTime(file)));
					}
				}
			}
		}
	}

	// Test method...
	void forceScan() throws InterruptedException, ExecutionException {
		scanExecutor.submit(this::scan).get();
	}
}
