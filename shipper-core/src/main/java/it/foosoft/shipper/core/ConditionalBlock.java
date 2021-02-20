package it.foosoft.shipper.core;

import it.foosoft.shipper.api.PipelineComponent;

class ConditionalBlock<T extends PipelineComponent> {
	LogicalExpression expr;
	Stage<T> stage;

	void start() {
		stage.start();
	}

	void stop() {
		stage.stop();
	}
}