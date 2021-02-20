package it.foosoft.shipper.api;

public interface FieldRef {

	void remove(Event e);

	void set(Event e, Object value);

	Object get(Event e);

}