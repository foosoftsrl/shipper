package it.foosoft.shipper.plugins.mutate;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRef;

public class CopyField implements EventProcessor {

	private FieldRef sourceField;
	private FieldRef targetField;

	public CopyField(FieldRef sourceField, FieldRef targetField) {
		this.sourceField = sourceField;
		this.targetField = targetField;
	}

	@Override
	public void process(Event e) {
		Object value = sourceField.get(e);
		targetField.set(e, value);
	}

}
