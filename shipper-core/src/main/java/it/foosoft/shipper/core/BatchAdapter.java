package it.foosoft.shipper.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.BatchOutput;
import it.foosoft.shipper.api.BatchOutputContext;
import it.foosoft.shipper.api.Output;
import it.foosoft.shipper.api.Param;

/**
 * BatchAdapter's will expose a simple "push" interface to the pipeline, and a "pull" OutputContext to the BatchOutput plugin
 *  
 * @author luca
 */
public class BatchAdapter extends EventQueue implements Output, BatchOutputContext {

	private static final Logger LOG = LoggerFactory.getLogger(BatchAdapter.class);

	public BatchOutput innerOutput;

	@Param
	String id;
	
	public BatchAdapter(BatchOutput.Factory outputPlugin) {
		this(outputPlugin, 512);
	}
	
	public BatchAdapter(BatchOutput.Factory batchOutputFactory, int batchSize) {
		super(batchSize);
		this.innerOutput = batchOutputFactory.create(this);
	}

	@Override
	public void start() {
		innerOutput.start();
	}

	@Override
	public void stop() {
		try {
			super.shutdown();
		} catch (InterruptedException e) {
			LOG.warn("Interrupted while stopping the queue");
			Thread.currentThread().interrupt();
		}
		innerOutput.stop();
	}

}
