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
}
