package it.foosoft.shipper.api;

/**
 * A plugin manager is an object capable of plugin discovery.
 * 
 * Production implementations will typically look at classpath, or at plugin directories, but simpler implementation for unit tests
 * are also possible
 * 
 * 
 * @author luca
 *
 */
public interface PluginManager {

	Input.Factory createInputPlugin(String name);

	Filter.Factory createFilterPlugin(String name);

	PipelineComponent.Factory createOutputPlugin(String name);

}
