package it.foosoft.shipper.api;

import java.util.List;

public interface BatchOutputContext extends Context {
	List<Event> dequeue();
}
