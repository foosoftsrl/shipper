package it.foosoft.shipper.core;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.Output;

public final class SimpleOutput implements Output {

	private EventProcessor processor;

	public SimpleOutput(EventProcessor processor) {
		this.processor = processor;
	}
	@Override
	public void start() {
		// do nothing
	}

	@Override
	public void stop() {
		// do nothing
	}

	@Override
	public void process(Event e) {
		processor.process(e);
	}
	
}
