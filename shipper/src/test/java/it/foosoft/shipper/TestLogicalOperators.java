package it.foosoft.shipper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.core.ConditionalFilter;
import it.foosoft.shipper.core.EventImpl;
import it.foosoft.shipper.core.Pipeline;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.core.PipelineBuilder;
import it.foosoft.shipper.plugins.DefaultPluginFactory;

public class TestLogicalOperators {
	@Test
	public void test() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Pipeline pipeline = PipelineBuilder.parse(DefaultPluginFactory.INSTANCE, Configuration.MINIMAL, getClass().getResource("files/conditions.conf"));
		ConditionalFilter filter = (ConditionalFilter)pipeline.getFilteringStage().get(0);
		filter.start();
		
		Event evt1 = new EventImpl();
		evt1.setField("message", "#dropme");
		filter.process(evt1);
		assertTrue(evt1.canceled());
		
		Event evt2 = new EventImpl();
		evt2.setField("message", "dont drop me");
		filter.process(evt2);
		assertFalse(evt2.canceled());
		
		
	}
}
