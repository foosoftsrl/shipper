package it.foosoft.shipper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.InputContext;
import it.foosoft.shipper.api.Output;
import it.foosoft.shipper.api.PluginManager;
import it.foosoft.shipper.core.Pipeline.Configuration;

public class TestPipelineInputStage {
	class SimpleInput implements Input {
		public SimpleInput(InputContext ctx) {
		}
	}

	@Test
	public void test() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException, InterruptedException {
		String pipelineDef = """
		input {
	        test{
	           add_field => {
	             "a_field" => "a_value"
	           }
	           add_tag => ["a_tag"]
	           type => "a_type"
	        }
		}
		""";

		PluginManager manager = EasyMock.createMock(PluginManager.class);
		Input.Factory inputFactory = EasyMock.createMock(Input.Factory.class);
		Capture<InputContext> inputContextCapture = EasyMock.newCapture();

		EasyMock.expect(inputFactory.create(EasyMock.capture(inputContextCapture))).andReturn(new SimpleInput(null)).anyTimes();
		EasyMock.expect(manager.findInputPlugin("test")).andReturn(inputFactory).anyTimes();
		EasyMock.replay(manager);
		EasyMock.replay(inputFactory);
		
		LinkedBlockingQueue<Event> queue = new LinkedBlockingQueue<>();
		
		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
		pipeline.addOutput(new Output() {
			@Override
			public void process(Event e) {
				queue.add(e);
			}
		});
		pipeline.start();

		// Dispatch an event
		for(int i = 0; i < 1024; i++) {
			InputContext inputContext = inputContextCapture.getValue();
			Event evt = inputContext.createEvent();
			inputContext.processEvent(evt);
		}

		// Verify that the event is received at output
		Event e = queue.take();
		assertEquals("a_value", e.getField("a_field"));
		assertEquals("a_tag", e.tags().stream().collect(Collectors.joining(",")));
		pipeline.stop();
		
	}
}
