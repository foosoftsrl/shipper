package it.foosoft.shipper.plugins.mutate;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;

public class RemoveField implements EventProcessor {

	private String fieldName;

	public RemoveField(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public void process(Event e) {
		e.removeField(fieldName);
	}

}
