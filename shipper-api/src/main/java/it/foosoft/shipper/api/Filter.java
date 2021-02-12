package it.foosoft.shipper.api;

public interface Filter extends Plugin, EventProcessor {
	public interface Factory {
		Filter create();
	}
	
	public void process(Event e);
}
