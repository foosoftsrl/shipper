package it.foosoft.shipper.api;

/**
 * A PipelineComponent is a part of Shipper's pipeline, which may be in input, filter, or output stage
 * 
 * The lifecycle of a PipelineComponent is:
 * 
 * Instantiation -> start() -> ... -> stop()
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

}

