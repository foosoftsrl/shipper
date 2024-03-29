package it.foosoft.shipper.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.junit.jupiter.api.Test;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.core.EventImpl;
import it.foosoft.shipper.core.InvalidPipelineException;
import it.foosoft.shipper.core.Pipeline;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.core.PipelineBuilder;

class TestDissectFilter {
	@Test
	void testSplitWithTabs() throws IOException, InvalidPipelineException {
		Pipeline pipeline = PipelineBuilder.build(DefaultPluginFactory.INSTANCE, Configuration.MINIMAL, getClass().getResource("files/dissect.conf"));
		DissectFilter filter = (DissectFilter)pipeline.findFilterPluginById("dissect");
		filter.start();
		try(var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("files/vod01.msf.cdn.mediaset.net_20210208222701_219180n2126107.log")))) {
			String line  = reader.readLine();
			Event evt = new EventImpl(1000);
			evt.setField("message", line);
			filter.process(evt);
			assertEquals("https://vod01.msf.cdn.mediaset.net/farmunica/2021/02/847689_17779c2d0cbb65/hlsrc/w3/3555.ts", evt.getField("uri"));
		}
		filter.stop();
	}

	@Test
	void testSplitWithSlashes() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		DissectFilter filter = new DissectFilter();
		filter.mapping = Map.of("message", "/farmunica/%{?year}/%{?month}/%{cmsId}_%{jobId}/%{assetpath}");
		filter.start();
		Event evt = new EventImpl(1000);
		evt.setField("message", "/farmunica/2020/10/100_1abc/blabla/bloblo");
		filter.process(evt);
		assertEquals(null, evt.getField("year"));
		assertEquals(null, evt.getField("month"));
		assertEquals("100", evt.getField("cmsId"));
		assertEquals("1abc", evt.getField("jobId"));
		assertEquals("blabla/bloblo", evt.getField("assetpath"));
	}

	@Test
	void testPatternDoesNotEndWithField() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		DissectFilter filter = new DissectFilter();
		filter.mapping = Map.of("message", "/farmunica/%{?year}/%{?month}/%{cmsId}_%{jobId}/%{assetpath}#");
		filter.start();
		Event evt = new EventImpl(1000);
		evt.setField("message", "/farmunica/2020/10/100_1abc/blabla/bloblo#");
		filter.process(evt);
		assertEquals(null, evt.getField("year"));
		assertEquals(null, evt.getField("month"));
		assertEquals("100", evt.getField("cmsId"));
		assertEquals("1abc", evt.getField("jobId"));
		assertEquals("blabla/bloblo", evt.getField("assetpath"));
	}
}
