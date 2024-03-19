package it.foosoft.shipper.core;

import it.foosoft.shipper.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BatchAdapter's will expose a simple "push" interface to the pipeline, and a "pull" OutputContext to the BatchOutput plugin
 *  
 * @author luca
 */
public class BatchAdapter implements Output {

	private static final Logger LOG = LoggerFactory.getLogger(BatchAdapter.class);

	public BatchOutput innerOutput;

	@ConfigurationParm
	String id;

	public EventQueue queue;
	
	public BatchAdapter(BatchOutput.Factory outputPlugin) {
		this(outputPlugin, 512);
	}
	
	public BatchAdapter(BatchOutput.Factory batchOutputFactory, int batchSize) {
		this.queue = new EventQueue(batchSize);
		this.innerOutput = batchOutputFactory.create(queue);
	}

	@Override
	public void start() {
		innerOutput.start();
	}

	@Override
	public void stop() {
		try {
			queue.shutdown();
		} catch (InterruptedException e) {
			LOG.warn("Interrupted while stopping the queue");
			Thread.currentThread().interrupt();
		}
		innerOutput.stop();
	}

	@Override
	public void process(Event e) {
		queue.process(e);
	}

	public Integer queueSize() {
		return queue.queueSize();
	}
}
