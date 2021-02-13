package it.foosoft.shipper.core;

import javax.validation.constraints.NotNull;

import it.foosoft.shipper.api.Bag;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.InputContext;

public class InputContextImpl implements InputContext {
	private EventProcessor processor;
	private Bag stopStatus;
	private Bag startStatus;
	private String id;

	public InputContextImpl(@NotNull EventProcessor processor) {
		this(processor, null);
	}

	public InputContextImpl(@NotNull EventProcessor processor, Bag startStatus) {
		this.processor = processor;
		this.startStatus = startStatus;
	}

	@Override
	public void processEvent(Event evt) {
		processor.process(evt);
	}

	@Override
	public Bag createBag() {
		return new BagImpl();
	}
	
	@Override
	public void setStopStatus(Bag status) {
		this.stopStatus = status;
	}

	public Bag getStopStatus() {
		return stopStatus;
	}

	@Override
	public Event createEvent() {
		return new EventImpl();
	}

	@Override
	public Bag getStartStatus() {
		return startStatus;
	}

	public void setStartStatus(Bag props) {
		this.startStatus = props;
		
	}

	public String getId() {
		return this.id;
		
	}
	public void setId(String filterId) {
		this.id = filterId;
		
	}
	
}
