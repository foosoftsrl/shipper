package it.foosoft.shipper.core.rvalue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.RValue;

/**
 * An rvalue which accesses event's metadata
 * 
 * @author luca
 */
public class MetadataRefRvalue implements RValue {

	private String[] identifiers;

	public MetadataRefRvalue(List<String> subList) {
		identifiers = subList.toArray(String[]::new);
	}

	@Override
	public Collection<Object> evaluateToCollection(Event e) {
		throw new UnsupportedOperationException("Can't convert a complex field ref to a collection");
	}

	@Override
	public Object get(Event evt) {
		Object obj = evt.getMetadata(identifiers[0]);
		for(int i = 1; i < identifiers.length; i++) {
			if(!(obj instanceof Map)) {
				return null;
			}
			Map asMap = (Map)obj;
			obj = asMap.get(identifiers[i]);
		}
		return obj;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("[@metadata]");
		for(String identifier: identifiers) {
			buf.append("[").append(identifier).append("]");
		}
		return buf.toString();
	}

}