package it.foosoft.shipper.plugins;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.core.*;
import it.foosoft.shipper.core.Pipeline.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestJsonFilter {
	@Test
	void testJsonFilter() throws IOException, InvalidPipelineException {
		Pipeline pipeline = PipelineBuilder.build(DefaultPluginFactory.INSTANCE, Configuration.MINIMAL, """
			filter {
				json {
				    source => "message"
					id => "json"
				}
			}
			""");
		var filter = (JsonFilter) pipeline.findFilterPluginById("json");
		filter.start();
		try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("files/jsonfilter.json")))) {
			String line = reader.readLine();
			Event evt = new EventImpl(1000);
			evt.setField("message", line);
			filter.process(evt);
			assertNotNull(evt.getField("cs-ip"));
		}
		filter.stop();
	}

	@Test
	void testFailure() throws IOException, InvalidPipelineException {
		Pipeline pipeline = PipelineBuilder.build(DefaultPluginFactory.INSTANCE, Configuration.MINIMAL, """
			filter {
				json {
				    source => "message"
					id => "json"
				}
			}
			""");
		var filter = (JsonFilter) pipeline.findFilterPluginById("json");
		filter.start();
		try {
			Event evt = new EventImpl(1000);
			evt.setField("message", "wrong_json");
			filter.process(evt);
			assertEquals(1, evt.tags().size());
			assertTrue(evt.tags().contains("_jsonparsefailure"));
		} finally {
			filter.stop();
		}
	}

}