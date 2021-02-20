package it.foosoft.shipper.core;

import java.util.HashMap;
import java.util.Map;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FieldRef;

/**
 * A "reference" to a possibly nested event field  
 * 
 * @author luca
 *
 */
public class FieldRefImpl implements FieldRef {
	private String[] fields;

	/**
	 * Construct a FieldRef from its string representation
	 * 
	 * @param expr Either a bare "fieldName" or an expression such as [parent][child]
	 */
	public FieldRefImpl(String expr) {
		// split on ][, this is needed for nested
		fields = expr.split("\\]\\[");
		for(int i = 0; i < fields.length; i++) {
			// simply drop ']' and '[' we don't need them 
			String field = fields[i].replace("[", "");			
			field = field.replace("]", "");
			fields[i] = field;
		}
	}
	
	@Override
	public void remove(Event e) {
		if(fields.length == 1) {
			e.removeField(fields[0]);
		} 
		else {
			Object obj = e.getField(fields[0]);
			if(obj == null) {
				return;
			} 
			else if(!(obj instanceof Map)) {
				throw new IllegalStateException("Trying to modify a sub-field of a field of type " + obj.getClass());
			}
			for(int i = 1; i < fields.length - 1; i++) {
				Map map = (Map)obj;
				obj = map.get(fields[0]);
				if(obj == null) {
					return;
				} 
				else if(!(obj instanceof Map)) {
					throw new IllegalStateException("Trying to modify a sub-field of a field of type " + obj.getClass());
				}
			}
			Map map = (Map)obj;
			map.remove(fields[fields.length - 1]);
		}
	}

	@Override
	public void set(Event e, Object value) {
		if(fields.length == 1) {
			e.setField(fields[0], value);
		} else {
			Object obj = e.getField(fields[0]);
			if(obj == null) {
				obj = new HashMap<>();
				e.setField(fields[0], obj);
			} 
			else if(!(obj instanceof Map)) {
				throw new IllegalStateException("Trying to modify a sub-field of a field of type " + obj.getClass());
			}
			for(int i = 1; i < fields.length - 1; i++) {
				Map map = (Map)obj;
				obj = map.get(fields[0]);
				if(obj == null) {
					obj = new HashMap<>();
					map.put(fields[0], obj);
				} 
				else if(!(obj instanceof Map)) {
					throw new IllegalStateException("Trying to modify a sub-field of a field of type " + obj.getClass());
				}
			}
			Map map = (Map)obj;
			map.put(fields[fields.length - 1], value);
		}
	}

	@Override
	public Object get(Event e) {
		if(fields.length == 1) {
			return e.getField(fields[0]);
		} else {
			Object obj = e.getField(fields[0]);
			if(obj == null) {
				return null;
			} 
			else if(!(obj instanceof Map)) {
				throw new IllegalStateException("Trying to modify a sub-field of a field of type " + obj.getClass());
			}
			for(int i = 1; i < fields.length - 1; i++) {
				Map map = (Map)obj;
				obj = map.get(fields[0]);
				if(obj == null) {
					return null;
				} 
				else if(!(obj instanceof Map)) {
					throw new IllegalStateException("Trying to modify a sub-field of a field of type " + obj.getClass());
				}
			}
			Map map = (Map)obj;
			return map.get(fields[fields.length - 1]);
		}
	}
}
