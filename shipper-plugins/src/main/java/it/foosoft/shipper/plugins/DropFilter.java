package it.foosoft.shipper.plugins;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FilterPlugin;

public class DropFilter implements FilterPlugin {
	@Override
	public boolean process(Event e) {
		e.cancel();
		return true;
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
