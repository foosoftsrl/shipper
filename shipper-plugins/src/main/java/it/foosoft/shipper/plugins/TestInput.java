package it.foosoft.shipper.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.InputContext;
import it.foosoft.shipper.api.Param;

public class TestInput implements Input {
	private static final Logger LOG = LoggerFactory.getLogger(TestInput.class);

	@Param
	String path;
	
	@Param
	boolean loop = false;

	private InputContext ctx;

	AtomicBoolean stopRequest = new AtomicBoolean(false);

	ExecutorService executor = Executors.newCachedThreadPool(r->{
		Thread t = new Thread(r);
		t.setName("FileInput");
		return t;
	});

	public TestInput(InputContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void start() {
		executor.submit(this::doRead);
	}

	@Override
	public void stop() {
		stopRequest.set(true);
		executor.shutdown();
		try {
			if(!executor.awaitTermination(5, TimeUnit.SECONDS)) {
				LOG.warn("Stopping the worker task took too long, giving up");
			}
		} catch (InterruptedException e) {
			LOG.warn("Timeout while stopping");
		}
	}

	private Object doRead() throws FileNotFoundException, IOException {
		try {
			do {
				try(var reader = new BufferedReader(createReader())) {
					String line;
					while((line = reader.readLine()) != null) {
						Event evt = ctx.createEvent();
						evt.setField("message", line);
						ctx.processEvent(evt);
					}
				}
			} while(loop && !stopRequest.get());
		} catch(Exception e) {
			LOG.warn("File thread exiting due to exception {}", e.getMessage());
		}
		LOG.info("Exiting cleanly");
		return null;
	}

	private Reader createReader() throws FileNotFoundException, MalformedURLException {
		// This is just for unit tests... probably I should register an URLStreamHandler 
		// Probably, it's a giant security hole
		if(path.startsWith("classpath:")) {
			String replace = path.replace("classpath:", "");
			InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(replace);
			return new InputStreamReader(resourceAsStream);
		}
		return new FileReader(new File(path));
	}
	
}
