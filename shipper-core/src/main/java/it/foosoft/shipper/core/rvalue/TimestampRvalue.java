package it.foosoft.shipper.core.rvalue;

import java.util.Collection;
import java.util.Date;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FieldRef;
import it.foosoft.shipper.api.RValue;

/**
 * An rvalue which accesses event's timestamp
 * 
 * @author luca
 */
public class TimestampRvalue implements RValue, FieldRef {


	@Override
	public Collection<Object> evaluateToCollection(Event e) {
		throw new UnsupportedOperationException("Can't convert a complex field ref to a collection");
	}

	@Override
	public Object get(Event evt) {
		return evt.getTimestamp();
	}
	
	@Override
	public String toString() {
		return "@timestamp";
	}

	@Override
	public void remove(Event e) {
		throw new UnsupportedOperationException("Can't remove timestamp attribute");
	}

	@Override
	public void set(Event evt, Object value) {
		evt.setTimestamp((Date)value);
	}

}
