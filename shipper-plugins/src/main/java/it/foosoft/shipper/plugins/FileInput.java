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
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.InputContext;
import it.foosoft.shipper.api.Param;

public class FileInput implements Input {
	private static final Logger LOG = LoggerFactory.getLogger(FileInput.class);

	@Param
	String path;

	@Param
	Integer olderThan = 30;

	@Param
	boolean remove = false;

	@Param
	int threads = 1;
	
	@Param
	int scanPeriod = 60;

	private InputContext ctx;

	private ScheduledExecutorService executor;
	
	private Set<Path> lastScan = new HashSet<>();

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
		executor = Executors.newScheduledThreadPool(threads);
		executor.scheduleWithFixedDelay(this::scan, 0, scanPeriod, TimeUnit.SECONDS);
	}

	@Override
	public void stop() {
		executor.shutdown();
	}
	
	public void scan() {
		try {
		    var entries = new ArrayList<Entry>();
		    recursivelyScanDirectory(Path.of(path), entry->{
		    	entries.add(entry);
		    });
		    entries.sort(FileInput::compareLastModified);
		    updateTaskList(entries);
		} catch(Exception e) {
			e.printStackTrace();
			LOG.error("Failed getting remote file list {}", e.getMessage());
		}
	}

	private synchronized void updateTaskList(ArrayList<Entry> entries) {
		Set<Path> fileList = new HashSet<>();
		for(Entry e : entries) {
		    if(!lastScan.contains(e.path)) {
		    	executor.submit(()->{
		    		download(e);
		    		return null;
		    	});
		    }
	    	fileList.add(e.path);
		}
		lastScan = fileList;
	}

	private synchronized void removeTaskFor(Path path) {
		lastScan.remove(path);
	}

	private void download(Entry entry) {
		try {
			try(InputStream istr = new FileInputStream(entry.path.toFile().getAbsolutePath())) {
				try(GZIPInputStream gzipIn = new GZIPInputStream(istr)) {
					try(var reader = new InputStreamReader(gzipIn)) {
						try(var bufferedReader = new BufferedReader(reader)) {
							Event e = ctx.createEvent();
							String line;
							while((line = bufferedReader.readLine()) != null) {
								e.setField("message", line);
								ctx.processEvent(e);
							}
						}
					}
				}
			}
		} catch(Exception e) {
			LOG.error("Failed downloading {} task will be retried", entry.path);
			removeTaskFor(entry.path);
		}
	}

	
	LinkedBlockingQueue<ChannelSftp> sftpCache = new LinkedBlockingQueue<ChannelSftp>();
    
	private static int compareLastModified(Entry left, Entry right) {
		return left.lastModified.compareTo(right.lastModified);
	}
	
	private static void recursivelyScanDirectory(Path dir, Consumer<Entry> consumer) throws IOException {
		LOG.info("Scanning {}", dir);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for(Path file: stream) {
				if(file.getFileName().startsWith("."))
					continue;
				if(Files.isDirectory(file, null)) {
					 recursivelyScanDirectory(file, consumer);
				}
				else {
					// Uhm... still seconds... can't believe this
					consumer.accept(new Entry(file, Files.getLastModifiedTime(file)));
				}
			}
		}
	}
}
