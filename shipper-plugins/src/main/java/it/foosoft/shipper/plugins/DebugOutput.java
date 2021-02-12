package it.foosoft.shipper.plugins;

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
			System.err.println(writer.writeValueAsString(e));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
