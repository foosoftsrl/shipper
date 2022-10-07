package it.foosoft.shipper.core.modifiers;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.core.StringInterpolator;

public class AddTag implements EventProcessor {

	private String tag;
	private StringInterpolator interpolator;

	public AddTag(String tag) {
		this.tag = tag;
		this.interpolator = new StringInterpolator(tag);
	}

	@Override
	public void process(Event e) {
		e.addTag(interpolator.evaluate(e));
	}

}
