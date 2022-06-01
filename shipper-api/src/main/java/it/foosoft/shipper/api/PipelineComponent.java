package it.foosoft.shipper.api;

import java.util.function.Consumer;

/**
 * A PipelineComponent is a part of Shipper's pipeline, which may be in input, filter, or output stage
 * 
 * The lifecycle of a PipelineComponent is:
 * 
 * Instantiation -> postConstruct() -> start() -> ... -> stop()
 * 
 * @author luca
 *
 */
public interface PipelineComponent {
	public interface Factory {
	}

	/**
	 * Start component. This method is called only after configuration is complete
	 */
	public default void start() {
		// Default... does nothing. This default method is here to reduce boilerplate in unit tests
	}

	/**
	 * Stop component. Any ongoing activity must be interrupted.
	 * 
	 * @return a possibly null state which can be used to restart  
	 */
	public default void stop() {
		// Default... does nothing. This default method is here to reduce boilerplate in unit tests
	}

	/**
	 * Depth first iteration. Implementors must send to the consumer themselves and all inner components
	 * 
	 * @param consumer an instance of {@link Consumer} which will accepts found components
	 */
	public default void traverse(Consumer<PipelineComponent> consumer) {
		consumer.accept(this);
	}
}

