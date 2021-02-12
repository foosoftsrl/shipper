package it.foosoft.shipper.api;

public interface InputContext extends Context {
	void processEvent(Event e);

	Event createEvent();
}
