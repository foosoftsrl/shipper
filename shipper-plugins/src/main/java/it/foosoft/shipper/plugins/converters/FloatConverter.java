package it.foosoft.shipper.plugins.converters;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;

public class FloatConverter implements EventProcessor {

	private String attrName;

	public FloatConverter(String attrName) {
		this.attrName = attrName;
	}

	@Override
	public void process(Event e) {
		Object obj = e.getField(attrName);
		if(obj != null) {
			e.setField(attrName, Float.parseFloat(obj.toString()));
		}
	}
}
