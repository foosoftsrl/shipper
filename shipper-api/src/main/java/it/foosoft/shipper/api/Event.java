package it.foosoft.shipper.api;

import java.util.Date;
import java.util.Set;

public interface Event {

	Object getField(String key);

	default String getFieldAsString(String key) {
		Object obj = getField(key);
		if(obj instanceof String) {
			return (String)obj;
		}
		return null;
	}

	void setField(String key, Object value);

	/**
	 * Still have to decide if 
	 * @param fieldName
	 */
	void removeField(String fieldName);

	void setTimestamp(Date d);
	
	/**
	 * Set the event in canceled state. Propagation will stop (hopefully) immediately
	 */
	void cancel();
	
	/**
	 * Is the event canceled?
	 * 
	 * @return
	 */
	boolean canceled();

	default void removeFields(String[] fields) {
		for(String field: fields) {
			removeField(field);
		}
	}

	long getTimestamp();
	
	public Set<String> getTags();
	
	public void addTag(String tag);

	default boolean includes(String key) {
		return getField(key) != null;
	}

}