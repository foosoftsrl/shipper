package it.foosoft.shipper.api;

/**
 * An RValue... resolves to a String or to a String[]  
 * 
 * @author luca
 *
 */
public interface RValue {
	public Object get(Event e);
}
