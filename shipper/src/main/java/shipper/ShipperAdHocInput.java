package shipper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.InputContext;

public class ShipperAdHocInput implements Input {
	private static final Logger LOG = LoggerFactory.getLogger(ShipperAdHocInput.class);

	private InputContext ctx;

	ExecutorService executor = Executors.newSingleThreadExecutor();

	private String path;

	private Runnable onEnd;

	public ShipperAdHocInput(InputContext ctx, String path) {
		this(ctx, path, null);
	}

	public ShipperAdHocInput(InputContext ctx, String path, Runnable onEnd) {
		this.ctx = ctx;
		this.path = path;
		this.onEnd = onEnd;
	}

	@Override
	public void start() {
		executor.submit(this::doRead);
	}

	@Override
	public void stop() {
		executor.shutdownNow();
		try {
			if(!executor.awaitTermination(5, TimeUnit.SECONDS)) {
				LOG.warn("Stopping the worker task took too long, giving up");
			}
		} catch (InterruptedException e) {
			LOG.warn("Timeout while stopping");
		}
	}

	private Object doRead() {
		try {
			try(var reader = new BufferedReader(new FileReader(new File(path)))) {
				String line;
				while((line = reader.readLine()) != null) {
					Event evt = ctx.createEvent();
					evt.setField("message", line);
					ctx.processEvent(evt);
				}
			}
		} catch(IOException e) {
			LOG.warn("File thread exiting due to exception {}", e.getMessage());
		}
		LOG.info("Exiting cleanly");
		if(onEnd != null) {
			onEnd.run();
		}
		return null;
	}

}
