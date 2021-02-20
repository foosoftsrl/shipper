package it.foosoft.shipper.core;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.foosoft.shipper.api.Event;

@JsonInclude(Include.NON_NULL)
public class EventImpl implements Event {
	// A special kind of event which may be used to notify source drain
	public static final Event VOID = new EventImpl();

	private long timeStamp;

	private Map<String,Object> fields = new HashMap<>();

	private Map<String,Object> metadata = new HashMap<>();

	private Set<String> tags = null;

	private boolean canceled;

	public EventImpl() {
		this(System.currentTimeMillis());
	}

	public EventImpl(long timestamp) {
		this.timeStamp = timestamp;
	}


	public EventImpl withField(String key, Object value) {
		setField(key, value);
		return this;
	}

	@Override
	public void setField(String key, Object value) {
		fields.put(key, value);
	}

	@Override
	public void removeField(String key) {
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
	public Set<String> tags() {
		if(tags == null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(tags);
	}

	@JsonProperty("tags")
	public Set<String> getTags() {
		return tags;
	}

	@Override
	public void addTag(String tag) {
		if(tags == null) {
			tags = new HashSet<>();
		} else {
			tags = new HashSet<>(tags);
		}
		tags.add(tag);
	}

	@Override
	public @NotNull Set<String> fieldNames() {
		return Collections.unmodifiableSet(fields.keySet()); 
	}

	@Override
	public Object getMetadata(String name) {
		return metadata.get(name);
	}

	@Override
	public void setMetadata(String name, Object value) {
		metadata.put(name, value);
	}

	@Override
	public Map<String, Object> metadata() {
		return Collections.unmodifiableMap(metadata);
	}

	public EventImpl withMetadata(String name, String value) {
		setMetadata(name, value);
		return this;
	}

	@Override
	public @NotNull Map<String, Object> fields() {
		return Collections.unmodifiableMap(fields);
	}
}
