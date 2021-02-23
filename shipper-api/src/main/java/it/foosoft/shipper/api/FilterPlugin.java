package it.foosoft.shipper.api;

public interface FilterPlugin extends PipelineComponent {
	public interface Factory {
		FilterPlugin create();
	}
	
	/**
	 * Apply Filter's own business logic, possibly modifying the event
	 * 
	 * Depending on the result, the event is post-processed for common actions (add_tag...) 
	 * 
	 * @return true if processing is successful
	 */
	public boolean process(Event e);
}
