package it.foosoft.shipper.core.modifiers;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.core.PrintfInterpolator;

public class AddField implements EventProcessor {

	private String attrName;
	private PrintfInterpolator interpolator;

	public AddField(String attrName, String value) {
		this.attrName = attrName;
		this.interpolator = new PrintfInterpolator(value);
	}

	@Override
	public void process(Event e) {
		e.setField(attrName, interpolator.interpolate(e));
	}

}
