package it.foosoft.shipper.core;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.PipelineComponent;

public interface Filter extends PipelineComponent {
	public void process(Event e);
}
