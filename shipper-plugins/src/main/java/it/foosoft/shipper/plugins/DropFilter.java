package it.foosoft.shipper.plugins;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;

public class DropFilter implements Filter {
	@Override
	public void process(Event e) {
		e.cancel();
	}

	@Override
	public void start() {
		// Nothing to do...
	}

	@Override
	public void stop() {
		// Nothing to do...
	}
}
