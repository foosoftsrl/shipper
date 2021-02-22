package it.foosoft.shipper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Output;
import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.PluginManager;
import it.foosoft.shipper.core.Pipeline.Configuration;

public class TestOutputConditionals {

	PluginManager manager = EasyMock.createMock(PluginManager.class);
	
	class SimpleOutputImpl implements Output {
		int counter = 0;
		@ConfigurationParm
		String field;

		@Override
		public void process(Event e) {
			counter++;
		}
	}

	@Test
	public void testPipelineWithMatchRegexCondition() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		output {
		    if [message] =~ /^if/ {
		        out{field => "if"}
		    } else if [message] =~ /^elsif/ {
		        out{field => "elsif"}
		    } else {
		        out{field => "else"}
		    }
		}
		""";
		
		List<SimpleOutputImpl> outputs = new ArrayList<>();

		Output.Factory a = ()->{
			SimpleOutputImpl simpleOutputImpl = new SimpleOutputImpl();
			outputs.add(simpleOutputImpl);
			return simpleOutputImpl;
		};
		EasyMock.expect(manager.findOutputPlugin("out")).andReturn(a).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, new StringReader(pipelineDef));
		assertEquals(1, pipeline.getOutputStage().size());
		ConditionalOutput cFilter = (ConditionalOutput)pipeline.getOutputStage().get(0);
		assertEquals(2, cFilter.blocks.size());
		assertEquals(1, cFilter.elseStage.size());

		
		Event evt;

		evt = new EventImpl().withField("message", "if");
		cFilter.process(evt);
		assertEquals(1, outputs.get(0).counter);
		assertEquals(0, outputs.get(1).counter);
		assertEquals(0, outputs.get(2).counter);
		
		evt = new EventImpl().withField("message", "elsif");
		cFilter.process(evt);
		assertEquals(1, outputs.get(1).counter);

		evt = new EventImpl().withField("message", "else");
		cFilter.process(evt);
		assertEquals(1, outputs.get(2).counter);

	}

}
