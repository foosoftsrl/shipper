package it.foosoft.shipper.plugins;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPInputStream;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.Bag;
import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FileWalker;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.InputContext;

public class FileInput implements Input {
	private static final Logger LOG = LoggerFactory.getLogger(FileInput.class);

	// TODO: terrible code. It must be cleaned and put in SDK (shipper-api) as directory parsing
	// can be useful to more than one plugin
	// Ok, this sucks. But I want to go in production
	public static String antGlobToRegexp(String globExpression) {
    	globExpression = globExpression.replace("**/", "<anydir>");
    	globExpression = globExpression.replace("*", "<anychar>");
    	globExpression = globExpression.replace("<anydir>", "([^/]*/)*");
    	globExpression = globExpression.replace("<anychar>", "[^/]*");
    	return globExpression;
    }
	
    @NotNull
	@ConfigurationParm
	String path;

	public static enum FileCompleteAction { delete; }

	@ConfigurationParm
	FileCompleteAction file_completed_action = FileCompleteAction.delete;

	public static enum Mode { read; }

	@NotNull
    @ConfigurationParm
	Mode mode;

    @ConfigurationParm
	Object sincedb_clean_after;

    @ConfigurationParm
	int threads = 1;
	
	@ConfigurationParm
	int discover_interval = 15;

	@ConfigurationParm
	int randomWindow = 0;

	private InputContext ctx;

	// the result of the last scan
	private Set<Path> lastScan = new HashSet<>();

	// The files queued for download
	private PriorityBlockingQueue<Entry> taskQueue = new PriorityBlockingQueue<>(1, FileInput::compareLastModified);

	// executor which discovers new files
	private ScheduledExecutorService scanExecutor;

	List<Worker> workers = new ArrayList<>();
	
	private static class Entry {
		public static Entry STOP = new Entry(null, 0);
		final Path path;
		final long lastModified;
		final int startOffset;
		Entry(Path file, long lastModified) {
			this(file, lastModified, 0);
		}
		Entry(Path file, long lastModified, int startOffset) {
			this.path = file;
			this.lastModified = lastModified;
			this.startOffset = startOffset;
		}
	}

	public FileInput(InputContext ctx) {
		this.ctx = ctx;
	}

	class Worker {
		private Thread thread;
		private Entry currentEntry;
		public AtomicBoolean stopRequest = new AtomicBoolean();
		public Worker() {
			thread = new Thread(()->{
				try {
					run();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					LOG.info("Worker exiting on unexpected interrupted exception");
				}
			});
			thread.start();
		}

		public void stop() throws InterruptedException {
			stopRequest.set(true);
			thread.join();
		}

		private void run() throws InterruptedException {
			while(true) {
				var entry = taskQueue.take();
				if(entry == Entry.STOP || stopRequest.get()) {
					return;
				}
				download(entry);
			}
		}

		private void download(Entry entry) {
			LOG.info("Processing {}", entry.path);
			try {
				currentEntry = entry;
				int currentLine = 0;
				try(InputStream istr = getInputStream(entry)) {
					try(var reader = new InputStreamReader(istr)) {
						try(var bufferedReader = new BufferedReader(reader)) {
							String line;
							while((line = bufferedReader.readLine()) != null && !stopRequest.get()) {
								if(currentLine < entry.startOffset) {
									LOG.debug("Skipping already processed line");
								} else {
									Event e = ctx.createEvent();
									e.setField("message", line);
									e.setField("path", currentEntry.path.toString());
									ctx.processEvent(e);
								}
								currentLine++;
							}
						}
					}
				}
				// if we arrived here on a stop request and we re
				if(stopRequest.get()) {
					int restartLine = Math.max(currentLine, entry.startOffset);
					currentEntry = new Entry(currentEntry.path, currentEntry.lastModified, restartLine); 
				} else {
					currentEntry = null; 
					Files.deleteIfExists(entry.path);
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
		if(mode == null) {
			throw new IllegalArgumentException("mode attribute must be specified (and btw... it can only be 'read')");
		}
		if(FileCompleteAction.delete != file_completed_action) {
			throw new IllegalArgumentException("only delete is supported for file_complete_action");
		}
		int pos = path.indexOf("*");
		if(pos < 0) {
			throw new IllegalArgumentException("the path parameter should have a wildcard, or something like that");
		}
		Bag startStartus = ctx.getStartStatus();
		if(startStartus != null) {
			Bag progress = startStartus.getBagProperty("progress");
			if(progress != null) {
				for(String pathStr: progress.getPropertyNames()) {
					Long idx = progress.getNumericProperty(pathStr);
					if(idx != null) {
						Path path = Path.of(pathStr);
						// We put an entry both in lastScan, to avoid an entry to be recreated
						// and in the taskQueue, in order to have the entry executed
						// as soon as the workers start
						Entry entry = new Entry(path, 0, idx.intValue());
						lastScan.add(entry.path);
						taskQueue.add(entry);
						LOG.info("Restarting {} @ {}", entry.path, idx);
					}
				}
			}
		}

		scanExecutor = Executors.newScheduledThreadPool(1);
		scanExecutor.scheduleWithFixedDelay(this::scan, 0, discover_interval * 500, TimeUnit.MILLISECONDS);
		
		for(int i = 0; i < threads; i++) {
			workers.add(new Worker());
		}
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
	
	/**
	 *  Stop the filter as soon as possible, writing 
	 *  
	 *  This method will actually complete shortly (milliseconds) unless workers are blocked downstream
	 *  
	 */
	@Override
	public void stop() {
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

		// Clear the task queue... we don't want the workers to start anything 
		taskQueue.clear();

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
		    for(Path path: FileWalker.walk(this.path)) {
		    	// Should I log anything if this is not a regular file? it may really be annoying
		    	if(Files.isRegularFile(path)) {
			    	long lastModifiedTime = Files.getLastModifiedTime(path).toMillis();
			    	if(randomWindow > 0)
			    		lastModifiedTime += ThreadLocalRandom.current().nextLong(randomWindow * 1000l);
					entries.add(new Entry(path, lastModifiedTime));
		    	}
		    }
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
		return Long.compare(left.lastModified, right.lastModified);
	}
	
	// Test method...
	void forceScan() throws InterruptedException, ExecutionException {
		scanExecutor.submit(this::scan).get();
	}
}
