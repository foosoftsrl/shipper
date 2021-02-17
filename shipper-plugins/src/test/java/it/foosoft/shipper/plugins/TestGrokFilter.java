package it.foosoft.shipper.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.core.EventImpl;

public class TestGrokFilter {
	@Test
	public void testHlsOrigin() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		GrokFilter filter = new GrokFilter();
		filter.match = Map.of("assetpath", "^(?<cmsTag>\\w*)/(?:(?<hlsManifest>\\w*\\.m3u8)|(?<hlsLevel>\\w*)/(?:(?<hlsLevelManifest>\\w*\\.m3u8)|%{INT:hlsVideoChunk:int}\\.ts|%{INT:hlsAudioChunk:int}\\.aac))");
		filter.start();

		Event evt = new EventImpl(1000);
		evt.setField("assetpath", "cmsTag/manifest.m3u8");
		filter.process(evt);
		assertEquals("cmsTag", evt.getField("cmsTag"));
		assertEquals("manifest.m3u8", evt.getField("hlsManifest"));

		evt = new EventImpl(1000);
		evt.setField("assetpath", "cmsTag/l0/l0.m3u8");
		filter.process(evt);
		assertEquals("cmsTag", evt.getField("cmsTag"));
		assertEquals("l0", evt.getField("hlsLevel"));
		assertEquals("l0.m3u8", evt.getField("hlsLevelManifest"));

		evt = new EventImpl(1000);
		evt.setField("assetpath", "cmsTag/l0/2222.ts");
		filter.process(evt);
		assertEquals("cmsTag", evt.getField("cmsTag"));
		assertEquals("l0", evt.getField("hlsLevel"));
		assertEquals(2222, evt.getField("hlsVideoChunk"));
	}

	@Test
	public void testGrokOk() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		GrokFilter filter = new GrokFilter();
		filter.match = Map.of("message", "^%{INT:intField:int}$");
		filter.start();

		Event evt = new EventImpl(1000);
		evt.setField("message", "123");
		assertTrue(filter.process(evt));
		assertEquals("message,intField", evt.fieldNames().stream().collect(Collectors.joining(",")));
		assertEquals(123, evt.getField("intField"));
		assertEquals("", evt.tags().stream().collect(Collectors.joining(",")));
	}

	@Test
	public void testGrokMatchFail() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		GrokFilter filter = new GrokFilter();
		filter.match = Map.of("message", "^%{INT:intField:int}$");
		filter.start();

		Event evt = new EventImpl(1000);
		evt.setField("message", "xxx");
		assertFalse(filter.process(evt));
		assertEquals("message", evt.fieldNames().stream().collect(Collectors.joining(",")));
		assertEquals("_grokparsefailure", evt.tags().stream().collect(Collectors.joining(",")));
	}

	// Not sure of what Logstash does here... let's accept the 
	@DisplayName("Verify what happens when match is positive but type conversion fails")
	@Test
	public void testGrokConversionFail() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		GrokFilter filter = new GrokFilter();
		filter.match = Map.of("message", "^%{USERNAME:intField:int}$");
		filter.start();

		
		Event evt = new EventImpl(1000);
		evt.setField("message", "xxx");
		filter.process(evt);
		assertEquals("message,intField", evt.fieldNames().stream().collect(Collectors.joining(",")));
		assertEquals("xxx", evt.getField("intField"));
		assertEquals("", evt.tags().stream().collect(Collectors.joining(",")));
	}

}
