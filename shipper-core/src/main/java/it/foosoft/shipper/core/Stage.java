package it.foosoft.shipper.core;

import java.util.ArrayList;

import it.foosoft.shipper.api.Plugin;

public class Stage<T extends Plugin> extends ArrayList<T> implements Plugin {

	private Pipeline pipeline;

	public Stage(Pipeline pipeline) {
		this.pipeline = pipeline;
	}
	public Pipeline getPipeline() {
		return pipeline;
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
