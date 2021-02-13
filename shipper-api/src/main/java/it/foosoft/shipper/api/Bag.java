package it.foosoft.shipper.api;

import java.util.Set;

/**
 * A simple map-like structure for state serialization 
 *  
 * @author luca
 *
 */
public interface Bag {
	void setStringProperty(String key, String value);
	void setNumericProperty(String key, long value);
	void setBagProperty(String key, Bag value);
	
	Long getNumericProperty(String key);
	String getStringProperty(String key);
	Bag getBagProperty(String key);
	Set<String> getPropertyNames();
}
