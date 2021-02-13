package it.foosoft.shipper.api;

/**
 *
 * Interface which provides platform functionalities to inputs   
 * 
 * @author luca
 */
public interface InputContext extends Context {
	/**
	 * Feed the pipeline with an event. This method will block when downstream filters / outputs can't keep the pace
	 * 
	 * @param e
	 */
	void processEvent(Event e);

	/**
	 * Create an event
	 */
	Event createEvent();
	
	/**
	 * Create a bag object which can be used for serialization
	 * @return
	 */
	Bag createBag();
	
	/**
	 * Store a status which will be available for a later restart
	 * 
	 * Input filters should use this feature to allow faster stop / start cycles
	 * 
	 * @param bag
	 */
	void setStopStatus(Bag bag);

	/**
	 * Get the status previously stored (in a previous execution) 
	 * 
	 * @param bag
	 */
	Bag getStartStatus();
}