package it.foosoft.shipper.api;

import java.util.Collection;

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
	
	/**
	 * Evaluate this reference to a collection. i.e. String-->Collection<String> int-->Collection<Integer> and so on...
	 * 
	 * No idea what to do for maps or other fancy types
	 *  
	 * @return
	 */
	public Collection<Object> evaluateToCollection(Event e);
}
