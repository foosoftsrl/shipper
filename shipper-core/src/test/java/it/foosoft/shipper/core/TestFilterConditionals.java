package it.foosoft.shipper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FilterPlugin;
import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.PluginManager;
import it.foosoft.shipper.core.Pipeline.Configuration;

public class TestFilterConditionals {

	PluginManager manager = EasyMock.createMock(PluginManager.class);
	
	class SimpleFilterImpl implements FilterPlugin {
		@ConfigurationParm
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

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
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

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
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

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
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

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
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

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
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

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
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

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
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
	public void testPipelineWithParentheses() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
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

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
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
	public void testPipelineWithNegateExpression() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		filter {
		    if (!([message] in ["else"])) {
		        set{field => "if"}
		    } else {
		        set{field => "else"}
		    }
		}
		""";
		
		EasyMock.expect(manager.findFilterPlugin("set")).andReturn(()->new SimpleFilterImpl()).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
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
	public void testPipelineWithNegateRValue() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		filter {
		    if ![message] {
		        set{field => "nomessage"}
		    } else {
		        set{field => "message"}
		    }
		}
		""";
		
		EasyMock.expect(manager.findFilterPlugin("set")).andReturn(()->new SimpleFilterImpl()).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
		assertEquals(1, pipeline.getFilteringStage().size());
		ConditionalFilter cFilter = (ConditionalFilter)pipeline.getFilteringStage().get(0);
		assertEquals(1, cFilter.blocks.size());
		assertEquals(1, cFilter.elseStage.size());

		
		Event evt;

		evt = new EventImpl().withField("message", "if");
		cFilter.process(evt);
		assertEquals("message", evt.getField("field"));
		
		evt = new EventImpl();
		cFilter.process(evt);
		assertEquals("nomessage", evt.getField("field"));
	}

	@Test
	public void testPipelineWithIfRValue() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		filter {
		    if [message] {
		        set{field => "message"}
		    } else {
		        set{field => "nomessage"}
		    }
		}
		""";
		
		EasyMock.expect(manager.findFilterPlugin("set")).andReturn(()->new SimpleFilterImpl()).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
		assertEquals(1, pipeline.getFilteringStage().size());
		ConditionalFilter cFilter = (ConditionalFilter)pipeline.getFilteringStage().get(0);
		assertEquals(1, cFilter.blocks.size());
		assertEquals(1, cFilter.elseStage.size());

		
		Event evt;

		evt = new EventImpl().withField("message", "if");
		cFilter.process(evt);
		assertEquals("message", evt.getField("field"));
		
		evt = new EventImpl();
		cFilter.process(evt);
		assertEquals("nomessage", evt.getField("field"));
	}

	@Test
	public void testPipelineWithCompareExpression() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		String pipelineDef = """
		filter {
		    if [message] == "test" {
		        set{field => "match"}
		    } else {
		        set{field => "nomatch"}
		    }
		}
		""";
		
		EasyMock.expect(manager.findFilterPlugin("set")).andReturn(()->new SimpleFilterImpl()).anyTimes();
		EasyMock.replay(manager);

		Pipeline pipeline = PipelineBuilder.build(manager, Configuration.MINIMAL, pipelineDef);
		assertEquals(1, pipeline.getFilteringStage().size());
		ConditionalFilter cFilter = (ConditionalFilter)pipeline.getFilteringStage().get(0);
		assertEquals(1, cFilter.blocks.size());
		assertEquals(1, cFilter.elseStage.size());

		
		Event evt;

		evt = new EventImpl().withField("message", "test");
		cFilter.process(evt);
		assertEquals("match", evt.getField("field"));
		
		evt = new EventImpl().withField("message", "other");
		cFilter.process(evt);
		assertEquals("nomatch", evt.getField("field"));
	}

}
