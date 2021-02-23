package it.foosoft.shipper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Output;
import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.PluginManager;
import it.foosoft.shipper.api.StringProvider;
import it.foosoft.shipper.core.Pipeline.Configuration;

public class TestPluginFieldInterpolation {
	PluginManager manager = EasyMock.createMock(PluginManager.class);

	String output;
	
	class SimpleOutputImpl implements Output {

		@ConfigurationParm
		StringProvider field;

		@Override
		public void process(Event e) {
		}
	}

	@Test
	public void testSimpleText() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		output {
	        out{field =>"test"}
		}
		""";
		
		SimpleOutputImpl simpleOutputImpl = new SimpleOutputImpl();
		Output.Factory a = ()->{
			return simpleOutputImpl;
		};
		EasyMock.expect(manager.findOutputPlugin("out")).andReturn(a).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
		assertEquals(1, pipeline.getOutputStage().size());

		assertEquals("test", simpleOutputImpl.field.evaluate(new EventImpl().withField("field", "value")));
	}

	@Test
	public void testNakedFieldExpansion() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		output {
	        out{field =>"%{field}"}
		}
		""";
		
		SimpleOutputImpl simpleOutputImpl = new SimpleOutputImpl();
		Output.Factory a = ()->{
			return simpleOutputImpl;
		};
		EasyMock.expect(manager.findOutputPlugin("out")).andReturn(a).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
		assertEquals(1, pipeline.getOutputStage().size());

		assertEquals("value", simpleOutputImpl.field.evaluate(new EventImpl().withField("field", "value")));
	}

	@Test
	public void testBracketFieldExpansion() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		output {
	        out{field =>"%{[field]}"}
		}
		""";
		
		SimpleOutputImpl simpleOutputImpl = new SimpleOutputImpl();
		Output.Factory a = ()->{
			return simpleOutputImpl;
		};
		EasyMock.expect(manager.findOutputPlugin("out")).andReturn(a).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
		assertEquals(1, pipeline.getOutputStage().size());

		assertEquals("value", simpleOutputImpl.field.evaluate(new EventImpl().withField("field", "value")));
	}

	@Test
	public void testMetadataFieldExpansion() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		output {
	        out{field =>"%{[@metadata][field]}"}
		}
		""";
		
		SimpleOutputImpl simpleOutputImpl = new SimpleOutputImpl();
		Output.Factory a = ()->{
			return simpleOutputImpl;
		};
		EasyMock.expect(manager.findOutputPlugin("out")).andReturn(a).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
		assertEquals(1, pipeline.getOutputStage().size());

		assertEquals("value", simpleOutputImpl.field.evaluate(new EventImpl().withMetadata("field", "value")));
	}

}
