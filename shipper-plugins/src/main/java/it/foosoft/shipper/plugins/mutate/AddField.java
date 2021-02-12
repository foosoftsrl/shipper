package it.foosoft.shipper.plugins.mutate;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.plugins.Interpolator;

public class AddField implements EventProcessor {

	private String attrName;
	private Interpolator interpolator;

	public AddField(String attrName, String value) {
		this.attrName = attrName;
		this.interpolator = new Interpolator(value);
	}

	@Override
	public void process(Event e) {
		e.setField(attrName, interpolator.interpolate(e));
	}

}
