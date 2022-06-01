package it.foosoft.shipper.core;

import java.util.ArrayList;
import java.util.function.Consumer;

import it.foosoft.shipper.api.PipelineComponent;

public class Stage<T extends PipelineComponent> extends ArrayList<T> implements PipelineComponent {

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

	@Override
	public void traverse(Consumer<PipelineComponent> consumer) {
		consumer.accept(this);
		for(PipelineComponent filter: this) {
			filter.traverse(consumer);
		}
	}
}
