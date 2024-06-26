package it.foosoft.shipper.core.modifiers;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRef;
import it.foosoft.shipper.core.StringInterpolator;

public class AddField implements EventProcessor {

	private FieldRef fieldRef;
	private StringInterpolator interpolator;

	public AddField(FieldRef attrName, String value) {
		this.fieldRef = attrName;
		this.interpolator = new StringInterpolator(value);
	}

	@Override
	public void process(Event e) {
		fieldRef.set(e, interpolator.evaluate(e));
	}

	public static EventProcessor create(FieldRef fieldRef, Object value) {
		if(value instanceof Boolean) {
			return evt->{
				fieldRef.set(evt, value);
			};
		} else if(value instanceof String) {
			return new AddField(fieldRef, (String)value);
		} else {
			throw new UnsupportedOperationException("Can't Add this field: " + value);
		}
	}

}
