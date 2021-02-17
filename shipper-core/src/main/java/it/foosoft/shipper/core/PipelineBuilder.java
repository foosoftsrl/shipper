package it.foosoft.shipper.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.logstash.ConfigLexer;
import com.logstash.ConfigParser;
import com.logstash.ConfigParser.Fieldref_elementContext;
import com.logstash.ConfigParser.HashContext;
import com.logstash.ConfigParser.Hash_elementContext;
import com.logstash.ConfigParser.In_expressionContext;
import com.logstash.ConfigParser.Logical_expressionContext;
import com.logstash.ConfigParser.Match_expressionContext;
import com.logstash.ConfigParser.Plugin_attributeContext;
import com.logstash.ConfigParser.Plugin_attribute_valueContext;
import com.logstash.ConfigParser.Plugin_declarationContext;
import com.logstash.ConfigParser.Plugin_definitionContext;
import com.logstash.ConfigParser.RvalueContext;
import com.logstash.ConfigParser.Stage_conditionContext;
import com.logstash.ConfigParser.Stage_declarationContext;
import com.logstash.ConfigParser.Stage_definitionContext;

import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Input.Factory;
import it.foosoft.shipper.api.Output;
import it.foosoft.shipper.api.PipelineComponent;
import it.foosoft.shipper.api.PluginManager;
import it.foosoft.shipper.api.RValue;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.core.expressions.AndExpression;
import it.foosoft.shipper.core.expressions.InExpression;
import it.foosoft.shipper.core.expressions.NotInExpression;
import it.foosoft.shipper.core.expressions.OrExpression;
import it.foosoft.shipper.core.expressions.RegexMatchExpression;
import it.foosoft.shipper.core.expressions.RegexNotMatchExpression;

/**
 * Class which builds a pipeline from a logstash-like pipeline definition
 * 
 * @author luca
 *
 */
public class PipelineBuilder {
	
	public static Logger LOG = LoggerFactory.getLogger(PipelineBuilder.class);

	public enum StageType {
		INPUT,
		FILTER,
		OUTPUT
	}

	private PluginManager pluginFactory;
	private Configuration configuration;
	
	public PipelineBuilder(PluginManager pluginFactory, Configuration conf) {
		this.pluginFactory = pluginFactory;
		this.configuration = conf;
	}

	public static Pipeline build(PluginManager pluginFactory, Configuration conf, File f) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		PipelineBuilder parser = new PipelineBuilder(pluginFactory, conf);
		try (InputStream istr = new FileInputStream(f)) {
			return parser.doBuild(CharStreams.fromStream(istr));
		}
	}
	public static Pipeline build(PluginManager pluginFactory, Configuration conf, URL url) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Objects.requireNonNull(url, "Invalid null URL specified");
		PipelineBuilder parser = new PipelineBuilder(pluginFactory, conf);
		try (InputStream istr = url.openStream()) {
			return parser.doBuild(CharStreams.fromStream(istr));
		}
	}

	public static Pipeline build(PluginManager pluginFactory, Configuration conf, InputStream istr) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Objects.requireNonNull(istr, "Invalid null stream specified");
		PipelineBuilder parser = new PipelineBuilder(pluginFactory, conf);
		return parser.doBuild(CharStreams.fromStream(istr));
	}

	public static Pipeline build(PluginManager pluginFactory, Configuration conf, Reader reader) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Objects.requireNonNull(reader, "Invalid null reader specified");
		PipelineBuilder parser = new PipelineBuilder(pluginFactory, conf);
		return parser.doBuild(CharStreams.fromReader(reader));
	}

	private Pipeline doBuild(CharStream fromStream) throws JsonParseException, JsonMappingException, IOException {
		Pipeline pipeline = new Pipeline(configuration);
		ConfigLexer lexer = new ConfigLexer(fromStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ConfigParser p = new ConfigParser(tokens);
		for (Stage_declarationContext stage : p.config().stage_declaration()) {
			StageType stageType = getStageType(stage);
			Stage_definitionContext definition = stage.stage_definition();
			if(stageType == StageType.INPUT) {
				parseInputStageDefinition(pipeline, definition);
			} 
			else if(stageType == StageType.FILTER) {
				parseFilteringStage(pipeline.getFilteringStage(), definition);
			}
			else if(stageType == StageType.OUTPUT) {
				parseOutputStageDefinition(pipeline, definition);
			}

		}
		return pipeline;
	}

	/**
	 * Parse the content of input stage, which is more or less what other people call "statement-block"
	 *  
	 * In input stage we do not support if statement (does it make sense?)
	 *  
	 * @param pipeline the pipeline in which input blocks are stored 
	 * @param definition the definition of the stage
	 * 
	 */
	private void parseInputStageDefinition(Pipeline pipeline, Stage_definitionContext definition) {
		for(var child: definition.getRuleContexts(ParserRuleContext.class)) {
			if(!(child instanceof Plugin_declarationContext)) {
				throw new UnsupportedOperationException("No support for conditionals in input/output");
			}
			var pluginDecl = (Plugin_declarationContext)child;
			LOG.info("Input plugin " + pluginDecl.IDENTIFIER());
			Factory inputPlugin = pluginFactory.findInputPlugin(pluginDecl.IDENTIFIER().getText());
			pipeline.addInput(inputPlugin, input->parsePluginConfig(input, input.wrapped, pluginDecl));
		}
	}

	/**
	 * Parse the content of output stage, which is more or less what other people call "statement-block"
	 *  
	 * In output stage we do not support if statement (does it make sense?)
	 *  
	 * @param pipeline the pipeline in which output blocks are stored
	 * @param definition the definition of the stage
	 */
	private void parseOutputStageDefinition(Pipeline pipeline, Stage_definitionContext definition) {
		for(var child: definition.getRuleContexts(ParserRuleContext.class)) {
			if(!(child instanceof Plugin_declarationContext)) {
				throw new UnsupportedOperationException("No support for conditionals in output stage");
			}
			var pluginDecl = (Plugin_declarationContext)child;
			LOG.info("Output plugin " + pluginDecl.IDENTIFIER());
			PipelineComponent.Factory outputPlugin = pluginFactory.findOutputPlugin(pluginDecl.IDENTIFIER().getText());
			Output output = pipeline.addOutput(outputPlugin);
			if(output instanceof BatchAdapter) {
				BatchAdapter adapter = (BatchAdapter)output;
				parsePluginConfig(null, adapter.innerOutput, pluginDecl);
			}
		}
	}

	/**
	 * Parse the content of a filtering stage, which is more or less what other people call "statement-block" 
	 * 
	 * Filtering stage parsing is a bit involved, as blocks can be nested due to conditionals  
	 * 
	 * @param stage the stage in which to insert blocks. May be the pipeline's filtering stage, or a conditional
	 * stage block
	 * @param definition
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	private void parseFilteringStage(Stage<Filter> stage, Stage_definitionContext definition) {
		for(int i = 0; i < definition.getChildCount(); i++) {
			ParseTree child = definition.getChild(i);
			if(child instanceof Stage_conditionContext) {
				var condition = (Stage_conditionContext)child;
				if(condition.IF() != null) {
					if(condition.logical_expression().size() == 0)
						throw new IllegalStateException("Missing if expression!");
					if((condition.stage_definition().size() - condition.logical_expression().size()) / 2 != 0)
						throw new IllegalStateException("Mismatch between expression and body count!");

					ConditionalFilter filter = new ConditionalFilter(stage.getPipeline());
					for(int blockIdx = 0; blockIdx < condition.logical_expression().size(); blockIdx++) {
						ConditionalFilter.ConditionalBlock block = new ConditionalFilter.ConditionalBlock();
						Logical_expressionContext exprCtx = condition.logical_expression(blockIdx);
						block.expr = createLogicalExpression(exprCtx); 
						block.stage = new Stage<>(stage.getPipeline());
						parseFilteringStage(block.stage, condition.stage_definition(blockIdx));
						filter.blocks.add(block);
					}

					if(condition.stage_definition().size() > condition.logical_expression().size()) {
						filter.elseStage = new Stage<>(stage.getPipeline());
						parseFilteringStage(filter.elseStage, condition.stage_definition(condition.stage_definition().size() - 1));
					}
					stage.add(filter);
				} else {
					throw new UnsupportedOperationException("No support for else");
				}
			}
			else if(child instanceof Plugin_declarationContext) {
				var pluginDecl = (Plugin_declarationContext)child;
				LOG.info("Filter plugin " + pluginDecl.IDENTIFIER());
				it.foosoft.shipper.api.Filter.Factory filterPlugin = pluginFactory.findFilterPlugin(pluginDecl.IDENTIFIER().getText());
				Filter filter = filterPlugin.create();
				FilterWrapper wrapper = new FilterWrapper(filter);
				stage.add(wrapper);
				parsePluginConfig(wrapper, filter, pluginDecl);
			}
		}
	}

	private LogicalExpression createLogicalExpression(Logical_expressionContext exprCtx) {
		ParserRuleContext rule = exprCtx.getRuleContext(ParserRuleContext.class, 0);
		if(rule instanceof Match_expressionContext) {
			Match_expressionContext match = (Match_expressionContext)rule;
			RvalueContext rValue = match.rvalue();
			String regexp;
			if(match.REGEX() != null) {
				regexp = extractStringContent(match.REGEX());
			}
			else if(match.STRING() != null) {
				regexp = extractStringContent(match.STRING());
			} 
			else {
				throw new UnsupportedOperationException("Unsupported right argument for regex match expressions");
			}
			if(match.MATCH() != null) {
				return new RegexMatchExpression(makeRValue(rValue), regexp);
			} 
			else if(match.NOT_MATCH() != null) {
				return new RegexNotMatchExpression(makeRValue(rValue), regexp);
			} 
			else { 
				throw new UnsupportedOperationException("Unsupported regexp match type");
			}

		} else if(rule instanceof In_expressionContext){
			In_expressionContext in = (In_expressionContext)rule;
			ensure2Children(in.rvalue());
			RvalueContext left = in.rvalue().get(0);
			RvalueContext right = in.rvalue().get(1);
			if(in.NOT() != null)
				return new NotInExpression(makeRValue(left), makeRValue(right));
			else
				return new InExpression(makeRValue(left), makeRValue(right));
		} 
		else if(exprCtx.OR() != null) {
			List<Logical_expressionContext> children = exprCtx.getRuleContexts(Logical_expressionContext.class);
			ensure2Children(children);
			return new OrExpression(createLogicalExpression(children.get(0)), createLogicalExpression(children.get(1)));
		}
		else if(exprCtx.AND() != null) {
			List<Logical_expressionContext> children = exprCtx.getRuleContexts(Logical_expressionContext.class);
			ensure2Children(children);
			return new AndExpression(createLogicalExpression(children.get(0)), createLogicalExpression(children.get(1)));
		}
		else if(exprCtx.LPAREN() != null) {
			List<Logical_expressionContext> children = exprCtx.getRuleContexts(Logical_expressionContext.class);
			if(children.size() != 1) {
				throw new UnsupportedOperationException("Can't find a single expression inside parentheses");
			}
			return createLogicalExpression(children.get(0));
		}
		else {
			throw new UnsupportedOperationException("Unrecognized logical operator");
		}
	}

	private <T> void ensure2Children(List<T> children) {
		if(children.size() != 2) {
			throw new UnsupportedOperationException("Can't find two operators for a binary operator");
		}
	}

	private RValue makeRValue(RvalueContext ctx) {
		if(ctx.STRING() != null) {
			return new StringRValue(extractStringContent(ctx.STRING()));
		} else if(ctx.array() != null) {
			List<String> s = new ArrayList<>();
			for (var item : ctx.array().array_element()) {
				if (item.STRING() == null) {
					throw new RuntimeException("No support for non-string in arrays");
				}
				s.add(extractStringContent(item.STRING()));
			}
			return new RValueArrayOfStrings(s.toArray(String[]::new));
		} else if(ctx.fieldref() != null) {
			List<Fieldref_elementContext> elementList = ctx.fieldref().fieldref_element();
			String[] identifiers = elementList.stream().map(e->e.IDENTIFIER().getText()).toArray(String[]::new);
			if(identifiers.length == 0) {
				throw new UnsupportedOperationException("No identifiers in fieldRef expression");
			}
			else if(identifiers.length == 1) {
				// Special (faster) case for single identifier
				return new FieldRefRValue(identifiers[0]);
			} 
			else {
				return new ComplexFieldRefRvalue(identifiers);
			}
		}
		throw new UnsupportedOperationException("Unsupported rvalue");
	}

	private void parsePluginConfig(Object wrapper, Object plugin, Plugin_declarationContext pluginDecl) {
		Plugin_definitionContext pluginDefinition = pluginDecl.plugin_definition();
		try {
			for (Plugin_attributeContext attribute : pluginDefinition.plugin_attribute()) {
				if (wrapper != null && haveField(wrapper.getClass(), attribute.IDENTIFIER().getText())) {
					setObjectParameter(wrapper, attribute);
				} else if(haveField(plugin.getClass(), attribute.IDENTIFIER().getText())) {
					setObjectParameter(plugin, attribute);
				} else {
					throw new RuntimeException("plugin " + pluginDecl.IDENTIFIER() + " has no attribute '"
							+ attribute.IDENTIFIER().getText() + "'");
				}
			}
			validateNonNull(plugin);
		} catch(Exception e) {
			throw new RuntimeException("Failed parsing configuration", e);
		}
	}

	private void validateNonNull(Object plugin) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Object>> violations = validator.validate(plugin);
		for(var violation: violations) {
			LOG.warn("Validation failed for " + plugin.getClass() + ": " + violation.getPropertyPath() + " " + violation.getMessage());
		}
		if(!violations.isEmpty()) {
			throw new IllegalArgumentException("Invalid configuration. Parameters did not pass validation");
		}
		
	}

	private void setObjectParameter(Object plugin, Plugin_attributeContext attribute) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Plugin_attribute_valueContext value = attribute.plugin_attribute_value();
		LOG.info("  attribute " + attribute.IDENTIFIER() + " is " + value.getText());
		Field field = plugin.getClass().getDeclaredField(attribute.IDENTIFIER().getText());
		field.setAccessible(true);
		if (value.STRING() != null) {
			String extractStringContent = extractStringContent(value.STRING());
			setFieldValueString(plugin, field, extractStringContent);
		} else if (value.DECIMAL() != null) {
			field.set(plugin, Integer.parseInt(value.DECIMAL().getText()));
		} else if (value.array() != null) {
			List<String> s = new ArrayList<>();
			for (var item : value.array().array_element()) {
				if (item.STRING() == null) {
					throw new RuntimeException("No support for non-string in arrays");
				}
				s.add(extractStringContent(item.STRING()));
			}
			field.set(plugin, s.stream().toArray(String[]::new));
		} else if(value.hash() != null) {
			HashContext hashCtx = value.hash();
			Map<String,String> kv = new HashMap<>();
			for(Hash_elementContext elmCtx: hashCtx.hash_element()) {
				if(elmCtx.STRING() == null) {
					throw new UnsupportedOperationException("No support for this hash key type... expected a string");
				}
				String hashKey = extractStringContent(elmCtx.STRING());
				TerminalNode valueNode = elmCtx.plugin_attribute_value().STRING();
				if(valueNode == null) {
					throw new UnsupportedOperationException("No support for this hash value type... supporting a string");
				}
				kv.put(hashKey, extractStringContent(valueNode));
			}
			field.set(plugin, kv);
		} else if(value.IDENTIFIER() != null) {
			String identifier = value.IDENTIFIER().getText();
			if("true".equals(identifier)) {
				field.set(plugin, true);
			} else if("false".equals(identifier)) {
				field.set(plugin, false);
			} else {
				throw new UnsupportedOperationException("Unsupported identifier " + identifier);
			}
		} else {
			throw new UnsupportedOperationException("Unsupported plugin type");
		}
	}

	private void setFieldValueString(Object plugin, Field field, String extractStringContent)
			throws IllegalAccessException, InvocationTargetException {
		if(field.getType().isAssignableFrom(String.class)) {
			field.set(plugin, extractStringContent);
			return;
		}
		for(Method m: field.getType().getDeclaredMethods()) {
			if(m.getName().equals("valueOf") && m.getParameterCount() == 1 && m.getParameters()[0].getType() == String.class) {
				m.setAccessible(true);
				field.set(plugin, m.invoke(null, extractStringContent));
				return;
			}
		}
		throw new IllegalStateException("Don't know how to initialize field " + plugin.getClass() + "." + field.getName() + " with a string");
	}

	private static String extractStringContent(TerminalNode valueNode) {
		String hashValue = valueNode.getText();
		// TODO: should use a parser method to access the body of the string
		// Don't know if there's a cleaner way to do this...
		hashValue = hashValue.substring(1, hashValue.length() - 1);
		return hashValue;
	}

	private static StageType getStageType(Stage_declarationContext stage) {
		if(stage.INPUT() != null)
			return StageType.INPUT;
		if(stage.FILTER() != null)
			return StageType.FILTER;
		if(stage.OUTPUT() != null)
			return StageType.OUTPUT;
		throw new IllegalArgumentException("Unknown stage type");
	}

	private static boolean haveField(Class<? extends Object> clazz, String name) {
		for (var field : clazz.getDeclaredFields()) {
			if (field.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
