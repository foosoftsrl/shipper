package it.foosoft.shipper.api;

/**
 * An RValue... resolves to a String or to a String[], possibly depending on event contents
 * 
 * The most trivial example of an RValue is a constant, such as "foo"
 * another example of plugin is a field-based interpolator, such as "%{foo}", which provides the content of the field "foo  
 * 
 * @author luca
 *
 */
public interface RValue {
	/**
	 * Resolves the rvalue to a String or String[]
	 * 
	 * @param e
	 * @return
	 */
	public Object get(Event e);
}
