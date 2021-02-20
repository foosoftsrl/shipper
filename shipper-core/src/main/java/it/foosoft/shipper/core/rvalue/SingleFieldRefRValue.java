package it.foosoft.shipper.core.rvalue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.RValue;

public class SingleFieldRefRValue implements RValue {

	private String fieldName;

	public SingleFieldRefRValue(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public Object get(Event evt) {
		if("tags".equals(fieldName))
			return evt.tags();
		return evt.getField(fieldName);
	}
	
	@Override
	public String toString() {
		return "[" + fieldName + "]";
	}

	@Override
	public Collection<Object> evaluateToCollection(Event e) {
		Object obj = get(e);
		if(obj instanceof Collection) {
			return (Collection)obj;
		}
		if(obj == null)
			return Collections.emptyList();
		if(obj instanceof String) {
			return Collections.singleton(obj);
		}
		throw new UnsupportedOperationException("Can't convert this field ref to a collection");
	}
}
