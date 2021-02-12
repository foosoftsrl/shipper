package it.foosoft.shipper.plugins;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.InputContext;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.Param;

public class SftpInput implements Input {
	private static final Logger LOG = LoggerFactory.getLogger(SftpInput.class);

	@Param
	String username;

	@Param
	String password;

	@Param
	String remoteHost;
	
	@Param
	String remoteDirectory = "/";

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
	
	private Set<String> lastScan = new HashSet<>();

	private JSch jsch;
	
	private static class Entry {
		final String path;
		final long lastModified; // date in milliseconds
		Entry(String path, long lastModified) {
			this.path = path;
			this.lastModified = lastModified;
		}
	}

	public SftpInput(InputContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void start() {
	    jsch = new JSch();
		executor = Executors.newScheduledThreadPool(threads);
		executor.scheduleWithFixedDelay(this::scan, 0, scanPeriod, TimeUnit.SECONDS);
	}

	@Override
	public void stop() {
		executor.shutdown();
	}
	
	public void scan() {
		try {
		    Session jschSession = jsch.getSession(username, remoteHost);
		    jschSession.setConfig("StrictHostKeyChecking", "no");
		    jschSession.setPassword(password);
		    jschSession.connect();
		    ChannelSftp channel = (ChannelSftp) jschSession.openChannel("sftp");
		    channel.connect();
		    var entries = new ArrayList<Entry>();
		    recursivelyScanDirectory(channel, remoteDirectory, entry->{
		    	entries.add(entry);
		    });
		    entries.sort(SftpInput::compareLastModified);
		    updateTaskList(jschSession, entries);
		} catch(Exception e) {
			e.printStackTrace();
			LOG.error("Failed getting remote file list {}", e.getMessage());
		}
	}

	private synchronized void updateTaskList(Session jschSession, ArrayList<Entry> entries) {
		Set<String> fileList = new HashSet<>();
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

	private synchronized void removeTaskFor(String path) {
		lastScan.remove(path);
	}

	private void download(Entry entry) {
		try {
			ChannelSftp channel = getFreeChannel(jsch);
			try(InputStream istr = channel.get(entry.path)) {
				try(GZIPInputStream gzipIn = new GZIPInputStream(istr)) {
					try(var reader = new InputStreamReader(gzipIn)) {
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
			}
			sftpCache.put(channel);
		} catch(Exception e) {
			LOG.error("Failed downloading {} task will be retried", entry.path);
			removeTaskFor(entry.path);
		}
	}

	
	LinkedBlockingQueue<ChannelSftp> sftpCache = new LinkedBlockingQueue<ChannelSftp>();
    
	private synchronized ChannelSftp getFreeChannel(JSch jsch) throws JSchException {
		ChannelSftp channel = sftpCache.poll();
		if(channel == null) {
			Session jschSession;
			jschSession = jsch.getSession(username, remoteHost);
			jschSession.setConfig("StrictHostKeyChecking", "no");
			jschSession.setPassword(password);
			jschSession.connect();
			channel = (ChannelSftp) jschSession.openChannel("sftp");
			channel.connect();
		}
		return channel;
	}


	private static int compareLastModified(Entry left, Entry right) {
		return Long.compare(left.lastModified, right.lastModified);
	}
	
	private static void recursivelyScanDirectory(ChannelSftp channel, String remoteDirectory, Consumer<Entry> consumer) throws SftpException {
		LOG.info("Scanning {}", remoteDirectory);
		Vector<LsEntry> list = (Vector)channel.ls(remoteDirectory);
		for(LsEntry entry: list) {
			if(entry.getFilename().startsWith("."))
				continue;
			SftpATTRS attrs = entry.getAttrs();
			if(attrs.isDir()) {
				 recursivelyScanDirectory(channel, remoteDirectory + "/" + entry.getFilename(), consumer);
			}
			else {
				// Uhm... still seconds... can't believe this
				consumer.accept(new Entry(remoteDirectory + "/" + entry.getFilename(), attrs.getMTime() * 1000l));
			}
		}
	}
}
