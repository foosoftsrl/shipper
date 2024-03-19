package it.foosoft.shipper.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.core.EventImpl;
import it.foosoft.shipper.core.InvalidPipelineException;
import it.foosoft.shipper.core.Pipeline;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.core.PipelineBuilder;

class TestLogstashDissectFilter {
	@Test
	void testSplitWithTabs() throws IOException, InvalidPipelineException {
		Pipeline pipeline = PipelineBuilder.build(DefaultPluginFactory.INSTANCE, Configuration.MINIMAL, getClass().getResource("files/dissect_logstash.conf"));
		LogstashDissectFilter filter = (LogstashDissectFilter)pipeline.findFilterPluginById("dissect");
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
		LogstashDissectFilter filter = new LogstashDissectFilter();
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
		LogstashDissectFilter filter = new LogstashDissectFilter();
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
	
	@Test
	void testNested() {
		LogstashDissectFilter filter = new LogstashDissectFilter();
		filter.mapping.put("message",
		"%{timeStamp}\t%{+timeStamp}\t%{ip}\t%{method}\t%{path}\t%{response}\t%{bytesSent}\t%{responseTime}\t\"%{referer}\"\t\"%{userAgentLog}\"\t%{?cookie}\t%{?custom}\t%{ghostIp}\t%{?cacheStatus}\t\"%{host}\""				
	    );
		filter.mapping.put("path","/%{?dummy}/%{path}");
		filter.start();
		Event evt = new EventImpl(1000);
		evt.setField("message", "2019-02-11\t21:59:51\t94.23.36.99\tGET\t/ss.playready.vod.mediasetpremium/farmunica/2018/06/230023_164524b0f36a2f/smoothenc/sd_pr_mpl.ism/manifest\t403\t634\t0\t\"-\"\t\"Mozilla/5.0 (X11; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0\"\t\"-\"\t\"-\"\t92.122.122.156\t0\t\"vod03.msf.cdn.mediaset.net\"");
		filter.process(evt);
		assertEquals(null, evt.getField("year"));
		assertEquals(null, evt.getField("month"));
		assertEquals("farmunica/2018/06/230023_164524b0f36a2f/smoothenc/sd_pr_mpl.ism/manifest", evt.getField("path"));
	}

	@Test
	@DisplayName("Verify support for syntax such as %{+timeStamp}")
	void testAppendToField() throws IOException, InvalidPipelineException {
		LogstashDissectFilter filter = new LogstashDissectFilter();
		filter.mapping.put("message","%{timeStamp}_%{+timeStamp} %{aaa}");
		filter.start();
		Event evt = new EventImpl(1000);
		evt.setField("message", "2021-02-23_13:29:04 xyz");
		filter.process(evt);
		assertEquals(null, evt.getField("year"));
		assertEquals(null, evt.getField("month"));
		assertEquals("2021-02-23_13:29:04", evt.getField("timestamp"));
	}
}
