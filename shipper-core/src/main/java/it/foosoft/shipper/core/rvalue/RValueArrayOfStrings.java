package it.foosoft.shipper.core.rvalue;

import java.util.Arrays;
import java.util.Collection;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.RValue;

public class RValueArrayOfStrings implements RValue {


	private String[] array;

	public RValueArrayOfStrings(String[] array) {
		this.array = array;
	}

	@Override
	public Object get(Event e) {
		return array;
	}

	@Override
	public Collection<Object> evaluateToCollection(Event e) {
		return Arrays.asList(array);
	}

}
