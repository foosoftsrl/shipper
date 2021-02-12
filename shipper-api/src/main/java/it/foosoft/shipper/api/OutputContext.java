package it.foosoft.shipper.api;

import java.util.List;

public interface OutputContext extends Context {
	List<Event> dequeue();
}
