package it.foosoft.shipper.core;

import java.util.ArrayList;

import it.foosoft.shipper.api.PipelineComponent;

public class Stage<T extends PipelineComponent> extends ArrayList<T> implements PipelineComponent {

	public Stage() {
	}

	@Override
	public void start() {
		for(var item: this) {
			item.start();
		}
	}

	@Override
	public void stop() {
		for(var item: this) {
			item.stop();
		}
	}
}
