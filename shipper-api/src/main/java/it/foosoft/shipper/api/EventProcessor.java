package it.foosoft.shipper.api;

/**
 * 
 * An as simple as possible interface for objects which modify an event
 * 
 * @author luca
 *
 */
public interface EventProcessor {
	void process(Event e);
}
