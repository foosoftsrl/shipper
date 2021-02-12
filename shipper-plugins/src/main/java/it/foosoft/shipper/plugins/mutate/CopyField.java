package it.foosoft.shipper.plugins.mutate;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;

public class CopyField implements EventProcessor {

	private String sourceField;
	private String targetField;

	public CopyField(String sourceField, String targetField) {
		this.sourceField = sourceField;
		this.targetField = targetField;
	}

	@Override
	public void process(Event e) {
		Object attr = e.getField(sourceField);
		e.setField(targetField, attr);
	}

}
