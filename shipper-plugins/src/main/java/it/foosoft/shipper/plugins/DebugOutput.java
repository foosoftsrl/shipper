package it.foosoft.shipper.plugins;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Output;

public class DebugOutput implements Output {

	static ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public void process(Event e) {
		try {
			HashMap map = new HashMap<>();
			map.putAll(e.fields());
			map.put("@metadata", e.metadata());
			map.put("tags", e.tags());
			map.put("@timestamp", e.getTimestamp());
			System.err.println(writer.writeValueAsString(map));
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
	}

}
