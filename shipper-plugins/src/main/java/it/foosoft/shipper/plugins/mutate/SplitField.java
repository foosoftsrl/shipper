package it.foosoft.shipper.plugins.mutate;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRef;

public class SplitField implements EventProcessor {

	private FieldRef sourceField;
	private String separator;

	public SplitField(FieldRef sourceField, String separator) {
		this.sourceField = sourceField;
		this.separator = separator;
	}

	@Override
	public void process(Event e) {
		Object value = sourceField.get(e);
		if(value instanceof String text) {
			sourceField.set(e, text.split(separator));
		}
	}

}
