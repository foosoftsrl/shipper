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
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	// only new
	private Set<Path> lastScan = new HashSet<>();

	// executor which discovers new files
	private ScheduledExecutorService scanExecutor;

	// executor for file parsing
	private ExecutorService workerPool;

	private Pattern pattern;

	private Path baseDir;

	private static class Entry {
		final Path path;
		final FileTime lastModified;
		Entry(Path file, FileTime fileTime) {
			this.path = file;
			this.lastModified = fileTime;
		}
	}

	public FileInput(InputContext ctx) {
		this.ctx = ctx;
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
		baseDir = Path.of(path.substring(0,pos));
		String filter = path.substring(pos);
		String filterRegex = antGlobToRegexp(filter);
		pattern = Pattern.compile(filterRegex);
		scanExecutor = Executors.newScheduledThreadPool(1);
		scanExecutor.scheduleWithFixedDelay(this::scan, 0, discover_interval * 500, TimeUnit.MILLISECONDS);

		// We do not want the thread to be interrupted...
		workerPool = new ThreadPoolExecutor(threads, threads,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(),
                        new ThreadFactory() {
							
							@Override
							public Thread newThread(Runnable r) {
								return new Thread(r) {
									@Override
									public void interrupt() {
										// do nothing
									}
								};
							}
						});
				Executors.newFixedThreadPool(threads);
	}

	@Override
	public void stop() {
		stop(false);
	}

	void stopAndCompleteQueuedJobs() {
		stop(true);
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
		if(completeQueuedTasks)
			workerPool.shutdown();
		else
			workerPool.shutdownNow();
		try {
			if(!workerPool.awaitTermination(120, TimeUnit.SECONDS)) {
				LOG.warn("Stopping the worker tasks took too long, giving up");
			} else {
				LOG.info("Stopped file readers");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LOG.info("Stopped direcory scanner");
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

	private void updateTaskList(ArrayList<Entry> entries) {
		Set<Path> fileList = new HashSet<>();
		for(Entry e : entries) {
		    if(!lastScan.contains(e.path)) {
		    	workerPool.submit(()->{
		    		download(e);
		    		return null;
		    	});
		    }
	    	fileList.add(e.path);
		}
		lastScan = fileList;
	}

	private void download(Entry entry) {
		try {
			try(InputStream istr = getInputStream(entry)) {
				try(var reader = new InputStreamReader(istr)) {
					try(var bufferedReader = new BufferedReader(reader)) {
						String line;
						while((line = bufferedReader.readLine()) != null) {
							Event e = ctx.createEvent();
							e.setField("message", line);
							ctx.processEvent(e);
						}
					}
				}
			}
			Files.deleteIfExists(entry.path);
		} catch(Exception e) {
			LOG.error("Failed processing file {}", entry.path, e);
			// we remove the file from lastScan result
			// next discovery iteration will retry
			scanExecutor.submit(()->{
				lastScan.remove(entry.path);
			});
		}
	}

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
