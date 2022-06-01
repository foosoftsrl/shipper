package it.foosoft.shipper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FieldRefBuilder;
import it.foosoft.shipper.api.FilterPlugin;
import it.foosoft.shipper.api.Inject;
import it.foosoft.shipper.api.PluginManager;
import it.foosoft.shipper.core.Pipeline.Configuration;

class TestInjection {

	PluginManager manager = EasyMock.createMock(PluginManager.class);

	class InjectableFilter implements FilterPlugin {
		@Inject
		FieldRefBuilder fieldRefBuilder;
		
		@ConfigurationParm
		String field;

		@Override
		public boolean process(Event e) {
			fieldRefBuilder.createFieldRef(field).set(e, "value");
			return true;
		}
		
	}

	@Test
	void test() throws IOException, InvalidPipelineException {
		String pipelineDef = """
		filter {
	        inject{
	           field => "field"
	        }
		}
		""";
		
		InjectableFilter injectableFilter = new InjectableFilter();

		EasyMock.expect(manager.findFilterPlugin("inject")).andReturn(()->injectableFilter);
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
		assertEquals(1, pipeline.getFilteringStage().size());

		Event evt;

		evt = new EventImpl();
		injectableFilter.process(evt);
		assertEquals("value", evt.getField("field"));
		
	}
}
