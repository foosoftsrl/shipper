package it.foosoft.shipper.api;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * An event is a timestamped collection of key-value pairs which is generated in the input stage of the pipeline, is modified in the
 * filtering stage, and is eventually dispatched to the external world in the output stage 
 * 
 * @author luca
 *
 */
public interface Event {

	/**
	 * Get timestamp of this event
	 * @return
	 */
	long getTimestamp();

	/**
	 * Set the timesamp of this event
	 * 
	 * @param d a non null date
	 */
	void setTimestamp(@NotNull Date d);
	
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

	/**
	 * Get value of a field or null if not present.
	 * 
	 * The field can currently be... a String, or an integer, or a String[], or a Map. Things are not yet stable, unfortunately
	 * 
	 * @param name 
	 * @return
	 */
	Object getField(String name);

	/**
	 * Set the value of a field. Setting a field to null is not the same as removing the field
	 * 
	 * @param name field name
	 * @param value field vale
	 */
	void setField(String name, Object value);

	/**
	 * Remove a field. Still have to decide if this is equivalent to setting a field with null
	 *  
	 * @param fieldName
	 */
	void removeField(String fieldName);

	/**
	 * Get an immutable collection of tags. 
	 *  
	 * @return string collection of tags
	 */
	public @NotNull Set<String> tags();
	
	/**
	 * Get an immutable collection of field names. 
	 *  
	 * @return string collection of tags
	 */
	public @NotNull Set<String> fieldNames();

	/**
	 * Add a tag
	 * 
	 * @param tag
	 */
	public void addTag(String tag);


	/**
	 * Helper method to remove a few fields at once 
	 * 
	 * @param fields
	 */
	default void removeFields(String... fields) {
		for(String field: fields) {
			removeField(field);
		}
	}

	/**
	 * Helper method to test for presence of a field
	 * 
	 * @param key
	 * @return
	 */
	default boolean includes(String key) {
		return getField(key) != null;
	}

	/**
	 * Helper method to get a field value as a string, or null if not present / not a string 
	 * 
	 * @param key
	 * @return
	 */
	default String getFieldAsString(String key) {
		Object obj = getField(key);
		if(obj instanceof String) {
			return (String)obj;
		}
		return null;
	}
	
}