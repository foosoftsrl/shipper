package it.foosoft.shipper.plugins.converters;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRef;

public class IntegerConverter implements Converter {

	private FieldRef fieldRef;

	public IntegerConverter(FieldRef fieldRef) {
		this.fieldRef = fieldRef;
	}

	@Override
	public void process(Event e) {
		Object obj = fieldRef.get(e);
		if(obj != null) {
			fieldRef.set(e, Long.parseLong(obj.toString()));
		}
	}
	@Override
	public String targetType() {
		return "int";
	}
}
