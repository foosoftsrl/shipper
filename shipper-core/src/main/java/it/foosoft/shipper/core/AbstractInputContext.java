package it.foosoft.shipper.core;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.InputContext;

public abstract class AbstractInputContext implements InputContext {

	@Override
	public Event createEvent() {
		return new EventImpl();
	}

}
