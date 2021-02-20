package it.foosoft.shipper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Param;
import it.foosoft.shipper.api.PluginManager;
import it.foosoft.shipper.core.Pipeline.Configuration;

public class TestFilterConditionals {

	PluginManager manager = EasyMock.createMock(PluginManager.class);
	
	class SimpleFilterImpl implements Filter {
		@Param
		String field;

		@Override
		public boolean process(Event e) {
			e.setField("field", field);
			return true;
		}
	}

	@Test
	public void testPipelineWithMatchRegexCondition() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		filter {
		    if [message] =~ /^if/ {
		        set{field => "if"}
		    } else if [message] =~ /^elsif/ {
		        set{field => "elsif"}
		    } else {
		        set{field => "else"}
		    }
		}
		""";
		
		EasyMock.expect(manager.findFilterPlugin("set")).andReturn(()->new SimpleFilterImpl()).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, new StringReader(pipelineDef));
		assertEquals(1, pipeline.getFilteringStage().size());
		ConditionalFilter cFilter = (ConditionalFilter)pipeline.getFilteringStage().get(0);
		assertEquals(2, cFilter.blocks.size());
		assertEquals(1, cFilter.elseStage.size());

		
		Event evt;

		evt = new EventImpl().withField("message", "if");
		cFilter.process(evt);
		assertEquals("if", evt.getField("field"));
		
		evt = new EventImpl().withField("message", "elsif");
		cFilter.process(evt);
		assertEquals("elsif", evt.getField("field"));

		evt = new EventImpl().withField("message", "else");
		cFilter.process(evt);
		assertEquals("else", evt.getField("field"));

	}

	@Test
	public void testPipelineWithMatchStringCondition() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		filter {
		    if [message] =~ "if" {
		        set{field => "if"}
		    } else {
		        set{field => "else"}
		    }
		    
		}
		""";
		
		EasyMock.expect(manager.findFilterPlugin("set")).andReturn(()->new SimpleFilterImpl()).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, new StringReader(pipelineDef));
		assertEquals(1, pipeline.getFilteringStage().size());
		ConditionalFilter cFilter = (ConditionalFilter)pipeline.getFilteringStage().get(0);
		assertEquals(1, cFilter.blocks.size());
		assertEquals(1, cFilter.elseStage.size());

		
		Event evt;

		evt = new EventImpl().withField("message", "if");
		cFilter.process(evt);
		assertEquals("if", evt.getField("field"));
		
		evt = new EventImpl().withField("message", "else");
		cFilter.process(evt);
		assertEquals("else", evt.getField("field"));
	}

	@Test
	public void testPipelineWithNotMatchStringCondition() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		filter {
		    if [message] !~ "if" {
		        set{field => "else"}
		    } else {
		        set{field => "if"}
		    }
		    
		}
		""";
		
		EasyMock.expect(manager.findFilterPlugin("set")).andReturn(()->new SimpleFilterImpl()).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, new StringReader(pipelineDef));
		assertEquals(1, pipeline.getFilteringStage().size());
		ConditionalFilter cFilter = (ConditionalFilter)pipeline.getFilteringStage().get(0);
		assertEquals(1, cFilter.blocks.size());
		assertEquals(1, cFilter.elseStage.size());

		
		Event evt;

		evt = new EventImpl().withField("message", "if");
		cFilter.process(evt);
		assertEquals("if", evt.getField("field"));
		
		evt = new EventImpl().withField("message", "else");
		cFilter.process(evt);
		assertEquals("else", evt.getField("field"));
	}

	@Test
	public void testPipelineWithInCondition() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		filter {
		    if [message] in ["if","fi"] {
		        set{field => "if"}
		    } else {
		        set{field => "else"}
		    }
		}
		""";
		
		EasyMock.expect(manager.findFilterPlugin("set")).andReturn(()->new SimpleFilterImpl()).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, new StringReader(pipelineDef));
		assertEquals(1, pipeline.getFilteringStage().size());
		ConditionalFilter cFilter = (ConditionalFilter)pipeline.getFilteringStage().get(0);
		assertEquals(1, cFilter.blocks.size());
		assertEquals(1, cFilter.elseStage.size());

		
		Event evt;

		evt = new EventImpl().withField("message", "if");
		cFilter.process(evt);
		assertEquals("if", evt.getField("field"));
		
		evt = new EventImpl().withField("message", "fi");
		cFilter.process(evt);
		assertEquals("if", evt.getField("field"));

		evt = new EventImpl().withField("message", "else");
		cFilter.process(evt);
		assertEquals("else", evt.getField("field"));
	}

	@Test
	public void testPipelineWithNotInCondition() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		filter {
		    if [message] not in ["else","fi"] {
		        set{field => "if"}
		    } else {
		        set{field => "else"}
		    }
		}
		""";
		
		EasyMock.expect(manager.findFilterPlugin("set")).andReturn(()->new SimpleFilterImpl()).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, new StringReader(pipelineDef));
		assertEquals(1, pipeline.getFilteringStage().size());
		ConditionalFilter cFilter = (ConditionalFilter)pipeline.getFilteringStage().get(0);
		assertEquals(1, cFilter.blocks.size());
		assertEquals(1, cFilter.elseStage.size());

		
		Event evt;

		evt = new EventImpl().withField("message", "if");
		cFilter.process(evt);
		assertEquals("if", evt.getField("field"));
		
		evt = new EventImpl().withField("message", "fi");
		cFilter.process(evt);
		assertEquals("else", evt.getField("field"));

		evt = new EventImpl().withField("message", "else");
		cFilter.process(evt);
		assertEquals("else", evt.getField("field"));
	}

	@Test
	public void testPipelineWithOrCondition() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		filter {
		    if [message] in ["if"] or [message] in ["fi"] {
		        set{field => "if"}
		    } else {
		        set{field => "else"}
		    }
		}
		""";
		
		EasyMock.expect(manager.findFilterPlugin("set")).andReturn(()->new SimpleFilterImpl()).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, new StringReader(pipelineDef));
		assertEquals(1, pipeline.getFilteringStage().size());
		ConditionalFilter cFilter = (ConditionalFilter)pipeline.getFilteringStage().get(0);
		assertEquals(1, cFilter.blocks.size());
		assertEquals(1, cFilter.elseStage.size());

		
		Event evt;

		evt = new EventImpl().withField("message", "if");
		cFilter.process(evt);
		assertEquals("if", evt.getField("field"));
		
		evt = new EventImpl().withField("message", "fi");
		cFilter.process(evt);
		assertEquals("if", evt.getField("field"));

		evt = new EventImpl().withField("message", "else");
		cFilter.process(evt);
		assertEquals("else", evt.getField("field"));
	}

	@Test
	public void testPipelineWithOrConditionAndParentheses() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		filter {
		    if (([message] in ["if"]) or ([message] in ["fi"])) {
		        set{field => "if"}
		    } else {
		        set{field => "else"}
		    }
		}
		""";
		
		EasyMock.expect(manager.findFilterPlugin("set")).andReturn(()->new SimpleFilterImpl()).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, new StringReader(pipelineDef));
		assertEquals(1, pipeline.getFilteringStage().size());
		ConditionalFilter cFilter = (ConditionalFilter)pipeline.getFilteringStage().get(0);
		assertEquals(1, cFilter.blocks.size());
		assertEquals(1, cFilter.elseStage.size());

		
		Event evt;

		evt = new EventImpl().withField("message", "if");
		cFilter.process(evt);
		assertEquals("if", evt.getField("field"));
		
		evt = new EventImpl().withField("message", "fi");
		cFilter.process(evt);
		assertEquals("if", evt.getField("field"));

		evt = new EventImpl().withField("message", "else");
		cFilter.process(evt);
		assertEquals("else", evt.getField("field"));
	}

	@Test
	public void testPipelineWithAndConditionAndParentheses() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		filter {
		    if (([message] in ["if"]) and ([message] in ["if"])) {
		        set{field => "if"}
		    } else {
		        set{field => "else"}
		    }
		}
		""";
		
		EasyMock.expect(manager.findFilterPlugin("set")).andReturn(()->new SimpleFilterImpl()).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, new StringReader(pipelineDef));
		assertEquals(1, pipeline.getFilteringStage().size());
		ConditionalFilter cFilter = (ConditionalFilter)pipeline.getFilteringStage().get(0);
		assertEquals(1, cFilter.blocks.size());
		assertEquals(1, cFilter.elseStage.size());

		
		Event evt;

		evt = new EventImpl().withField("message", "if");
		cFilter.process(evt);
		assertEquals("if", evt.getField("field"));
		
		evt = new EventImpl().withField("message", "else");
		cFilter.process(evt);
		assertEquals("else", evt.getField("field"));
	}

	@Test
	public void testPipelineWithMatch() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		
	}

}
