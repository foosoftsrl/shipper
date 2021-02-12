package it.foosoft.shipper.plugins.converters;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;

public class IntegerConverter implements EventProcessor {

	private String attrName;

	public IntegerConverter(String attrName) {
		this.attrName = attrName;
	}

	@Override
	public void process(Event e) {
		Object obj = e.getField(attrName);
		if(obj != null) {
			e.setField(attrName, Integer.parseInt(obj.toString()));
		}
	}
}
