package it.foosoft.shipper.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.foosoft.shipper.core.EventImpl;
import it.foosoft.shipper.core.FieldRefBuilderImpl;
import it.foosoft.shipper.core.InvalidPipelineException;
import it.foosoft.shipper.core.Pipeline;
import it.foosoft.shipper.core.PipelineBuilder;
import it.foosoft.shipper.core.Pipeline.Configuration;

class TestDateFilter {

	private DateFilter createDateFilter() {
		return new DateFilter(new FieldRefBuilderImpl());
	}
	
	@Test
	void testParsing() throws IOException, InvalidPipelineException {

		String config = """
		filter {
		    date {
				match => ["timestamp", "dd/MMM/yyyy:HH:mm:ss.SSSZ"]
		    }
		}
		""";
		Pipeline pipeline = PipelineBuilder.build(DefaultPluginFactory.INSTANCE, Configuration.MINIMAL, config);
		DateFilter dateFilter = pipeline.findFilterPluginByClass(DateFilter.class);
		assertNotNull(dateFilter);
	}
	

	@Test
	void testLocale() throws URISyntaxException {
		DateFilter f = createDateFilter();
		f.locale = "en_US";
		f.match = new String[] {"timestamp", "dd/MMM/yyyy:HH:mm:ss.SSSZ"};
		f.postConstruct();

		EventImpl evt = new EventImpl().withField("timestamp", "17/Jan/2019:04:25:20.537+0000");
		f.process(evt);
		assertEquals(1547699120537l, evt.getTimestamp());
	}

	@Test
	void testLocaleIt() throws URISyntaxException {
		DateFilter f = createDateFilter();
		f.locale = "it";
		f.match = new String[] {"timestamp", "dd/MMM/yyyy:HH:mm:ss.SSSZ"};
		f.postConstruct();
		EventImpl evt = new EventImpl().withField("timestamp", "17/gen/2019:04:25:20.537+0000");
		f.process(evt);
		assertEquals(1547699120537l, evt.getTimestamp());
	}

	@Test
	void testTimezone() throws URISyntaxException {
		DateFilter f = createDateFilter();
		f.locale = "it";
		f.match = new String[] {"timestamp", "dd/MMM/yyyy:HH:mm:ss.SSSZ"};
		f.postConstruct();

		EventImpl evt = new EventImpl().withField("timestamp", "25/Feb/2021:14:06:02.202+0000");
		f.process(evt);
		assertEquals(1614261962202l, evt.getTimestamp());
	}

	// We had this situation in production... weird timezones. Can we ignore them?
	@Test
	void testIgnoreTimezone() throws URISyntaxException {
		DateFilter f = createDateFilter();
		f.locale = "it";
		f.timezone = "GMT";
		f.match = new String[] {"timestamp", "dd/MMM/yyyy:HH:mm:ss.SSS"};
		f.postConstruct();

		EventImpl evt = new EventImpl().withField("timestamp", "25/Feb/2021:14:06:02.202+000");
		f.process(evt);
		assertEquals(1614261962202l, evt.getTimestamp());
	}

	// Test "UNIX" timestamp syntax
	@Test
	void testUnix() throws URISyntaxException {
		DateFilter f = createDateFilter();
		f.match = new String[] {"timestamp", "UNIX"};
		f.postConstruct();
		EventImpl evt = new EventImpl().withField("timestamp", "1326149001.132");
		f.process(evt);
		assertEquals(1326149001132l, evt.getTimestamp());
	}

	// Test "UNIX" timestamp syntax
	@Test
	void testUnix_ms() throws URISyntaxException {
		DateFilter f = createDateFilter();
		f.match = new String[] {"timestamp", "UNIX_MS"};
		f.postConstruct();
		EventImpl evt = new EventImpl().withField("timestamp", "1326149001132");
		f.process(evt);
		assertEquals(1326149001132l, evt.getTimestamp());
	}
	
	@Test
	@DisplayName("Verify support for target parameter")
	void testTarget() throws URISyntaxException {
		DateFilter f = createDateFilter();
		f.match = new String[] {"timestamp", "UNIX"};
		f.target = "pippo";
		f.postConstruct();
		EventImpl evt = new EventImpl().withField("timestamp", "1326149001.132");
		f.process(evt);
		assertEquals(1326149001132l, ((Date)evt.getField("pippo")).getTime());
	}
	
}
