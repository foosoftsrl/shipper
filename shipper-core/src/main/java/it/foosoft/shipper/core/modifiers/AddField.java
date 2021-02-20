package it.foosoft.shipper.core.modifiers;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRef;
import it.foosoft.shipper.core.PrintfInterpolator;

public class AddField implements EventProcessor {

	private FieldRef fieldRef;
	private PrintfInterpolator interpolator;

	public AddField(FieldRef attrName, String value) {
		this.fieldRef = attrName;
		this.interpolator = new PrintfInterpolator(value);
	}

	@Override
	public void process(Event e) {
		fieldRef.set(e, interpolator.evaluate(e));
	}

}
