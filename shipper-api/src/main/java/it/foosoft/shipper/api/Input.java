package it.foosoft.shipper.api;

/**
 * An Input is a Shipper plugin capable of retrieving events from sources of various types (files, for example) and dispatch them to
 * Shipper's filtering stage   
 * 
 * @author luca
 */
public interface Input extends PipelineComponent {
	public interface Factory {
		Input create(InputContext ctx);
	}
}
