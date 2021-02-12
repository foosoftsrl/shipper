package it.foosoft.shipper.core;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.foosoft.shipper.api.Event;

public class EventImpl implements Event {
	private long timeStamp;

	private Map<String,Object> fields = new HashMap<>();

	private Set<String> tags = new HashSet<>();

	private boolean canceled;

	public EventImpl() {
		this(System.currentTimeMillis());
	}

	public EventImpl(long timestamp) {
		this.timeStamp = timestamp;
	}

	
	@Override
	public void setField(String key, Object value) {
		fields.put(key, value);
	}

	@Override
	public void removeField(String key) {
		if("tags".equals(key))
			throw new IllegalStateException("Can't remove tags field");
		fields.remove(key);
	}

	@Override
	public Object getField(String key) {
		return fields.get(key);
	}
	
	@JsonAnyGetter
	public Map<String,Object> getAttributes() {
		return fields;
	}

	@Override
	public void cancel() {
		this.canceled = true;
	}

	@Override
	public boolean canceled() {
		return this.canceled;
	}

	@Override
	public void setTimestamp(Date d) {
		this.timeStamp = d.getTime();
	}
	
	
	@JsonProperty("@timestamp")
	@Override
	public long getTimestamp() {
		return timeStamp;
	}

	@Override
	public Set<String> getTags() {
		return tags;
	}

	@Override
	public void addTag(String tag) {
		tags.add(tag);
	}
}
