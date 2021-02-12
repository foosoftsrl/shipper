package it.foosoft.shipper.core;

import javax.validation.constraints.NotNull;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;

public class InputContextImpl extends AbstractInputContext {
	private EventProcessor processor;

	public InputContextImpl(@NotNull EventProcessor processor) {
		this.processor = processor;
	}

	@Override
	public void processEvent(Event evt) {
		processor.process(evt);
	}
	
}
