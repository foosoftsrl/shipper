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

	Input.Factory findInputPlugin(String name);

	FilterPlugin.Factory findFilterPlugin(String name);

	PipelineComponent.Factory findOutputPlugin(String name);

}
