package it.foosoft.shipper.api;

/**
 * Generic Interface for resolving plugins.
 * 
 * Production implementations will typically look at classpath, or at plugin directories, but simpler implementation for unit tests
 * are also possible
 * 
 * @author luca
 *
 */
public interface PluginManager {

	Input.Factory createInputPlugin(String name);

	Filter.Factory createFilterPlugin(String name);

	Plugin.Factory createOutputPlugin(String name);

}
