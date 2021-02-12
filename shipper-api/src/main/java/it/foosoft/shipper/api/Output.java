package it.foosoft.shipper.api;

/**
 * Interface for event dispatching.
 * Plugins may implement this interface, but in most cases they will implement {@link BatchOutput}   
 * 
 * @author luca
 *
 */
public interface Output extends Plugin {
	public interface Factory {
		Output create();
	}

	/**
	 * Process a packet. Implementation *must* be thread safe.
	 * Event *must not* be modified 
	 * 
	 * @param e an event
	 */
	public void process(Event e);
}
