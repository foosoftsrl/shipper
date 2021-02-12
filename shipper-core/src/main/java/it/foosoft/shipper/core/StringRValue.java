package it.foosoft.shipper.core;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.RValue;

public class StringRValue implements RValue {

	private String str;

	public StringRValue(String str) {
		this.str = str;
	}

	@Override
	public Object get(Event e) {
		return str;
	}
	
	@Override
	public String toString() {
		return "\"" + str + "\"";
	}

}
