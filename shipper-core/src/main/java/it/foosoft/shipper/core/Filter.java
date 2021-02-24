package it.foosoft.shipper.core;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.PipelineComponent;

public interface Filter extends PipelineComponent {
	enum Result {
		CONTINUE,
		BREAK;
	}
	public Result process(Event e);
}
