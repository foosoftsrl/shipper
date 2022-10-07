package it.foosoft.shipper.plugins.mutate;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRef;
import it.foosoft.shipper.core.StringInterpolator;

public class ReplaceField implements EventProcessor {

	private FieldRef sourceField;
	private StringInterpolator interpolator;

	public ReplaceField(FieldRef sourceField, String withText) {
		this.sourceField = sourceField;
		this.interpolator = new StringInterpolator(withText);
	}

	@Override
	public void process(Event e) {
		Object value = sourceField.get(e);
		if(value instanceof String text) {
			sourceField.set(e, interpolator.evaluate(e));
		}
	}

}
