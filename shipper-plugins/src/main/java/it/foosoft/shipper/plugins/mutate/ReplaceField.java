package it.foosoft.shipper.plugins.mutate;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRef;
import it.foosoft.shipper.api.StringProvider;

public class ReplaceField implements EventProcessor {

	private FieldRef sourceField;
	private StringProvider interpolator;

	public ReplaceField(FieldRef sourceField, StringProvider withText) {
		this.sourceField = sourceField;
		this.interpolator = withText;
	}

	@Override
	public void process(Event e) {
		Object value = sourceField.get(e);
		if(value instanceof String text) {
			sourceField.set(e, interpolator.evaluate(e));
		}
	}

}
