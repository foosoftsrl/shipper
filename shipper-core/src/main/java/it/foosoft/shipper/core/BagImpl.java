package it.foosoft.shipper.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import it.foosoft.shipper.api.Bag;

public class BagImpl implements Bag {

	final Map<Object,Object> map = new HashMap<>(); 
	BagImpl() {
	}

	BagImpl(Map<Object,Object> map) {
		for(Map.Entry<Object, Object> entry : map.entrySet()) {
			if(entry.getValue() instanceof Map) {
				this.map.put(entry.getKey(), new BagImpl((Map)entry.getValue()));
			} else {
				this.map.put(entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
	public void setStringProperty(String key, String value) {
		map.put(key, value);
	}

	@Override
	public void setNumericProperty(String key, long value) {
		map.put(key, value);
	}

	@Override
	public void setBagProperty(String key, Bag value) {
		map.put(key, value);
	}

	@Override
	public Long getNumericProperty(String key) {
		Object obj = map.get(key);
		if(obj instanceof Integer)
			return ((Integer)obj).longValue();
		if(obj instanceof Long)
			return ((Long)obj).longValue();
		return null;
	}

	@Override
	public String getStringProperty(String key) {
		Object obj = map.get(key);
		if(obj instanceof String)
			return (String)obj;
		return null;
	}

	@Override
	public Bag getBagProperty(String key) {
		Object obj = map.get(key);
		if(obj instanceof Bag) {
			return (Bag)obj;
		}
		return null;
	}

	@JsonAnyGetter
	public Map<Object,Object> getMap() {
		return map;
	}

	@JsonAnySetter
	public void setMap(String key, Object value) {
		if(value instanceof Map) {
			map.put(key, new BagImpl((Map<Object,Object>)value));
		} else {
			map.put(key, value);
		}
	}

	@JsonIgnore
	@Override
	public Set<String> getPropertyNames() {
		return (Set)map.keySet();
	}
}
