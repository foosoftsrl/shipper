package it.foosoft.shipper.core.modifiers;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRef;

public class RemoveField implements EventProcessor {

	private FieldRef fieldName;

	public RemoveField(FieldRef fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public void process(Event e) {
		fieldName.remove(e);
	}

}
