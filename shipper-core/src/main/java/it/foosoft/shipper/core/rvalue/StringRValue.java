package it.foosoft.shipper.core.rvalue;

import java.util.Arrays;
import java.util.Collection;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.RValue;

/**
 * An RValue implementation which is a simple string
 * 
 * @author luca
 *
 */
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

	@Override
	public Collection<Object> evaluateToCollection(Event e) {
		return Arrays.asList(str);
	}

}
