package it.foosoft.shipper.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

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

import it.foosoft.shipper.api.BatchOutput;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.InputContext;
import it.foosoft.shipper.api.Output;
import it.foosoft.shipper.api.PipelineComponent;
import it.foosoft.shipper.api.PluginManager;
import it.foosoft.shipper.api.RValue;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.core.expressions.InExpression;
import it.foosoft.shipper.core.expressions.NotInExpression;
import it.foosoft.shipper.core.expressions.OrExpression;
import it.foosoft.shipper.core.expressions.RegexMatchExpression;
import it.foosoft.shipper.core.expressions.RegexNotMatchExpression;

public class PipelineBuilder {
	
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

	public static Pipeline parse(PluginManager pluginFactory, Configuration conf, File f) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		PipelineBuilder parser = new PipelineBuilder(pluginFactory, conf);
		try (InputStream istr = new FileInputStream(f)) {
			return parser.doParse(istr);
		}
	}
	public static Pipeline parse(PluginManager pluginFactory, Configuration conf, URL url) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Objects.requireNonNull(url, "Invalid null URL specified");
		PipelineBuilder parser = new PipelineBuilder(pluginFactory, conf);
		try (InputStream istr = url.openStream()) {
			return parser.doParse(istr);
		}
	}

	private Pipeline doParse(InputStream istr) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Pipeline pipeline = new Pipeline(configuration);
		ConfigLexer lexer = new ConfigLexer(CharStreams.fromStream(istr));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ConfigParser p = new ConfigParser(tokens);
		for (Stage_declarationContext stage : p.config().stage_declaration()) {
			StageType stageType = getStageType(stage);
			Stage_definitionContext definition = stage.stage_definition();
			if(stageType == StageType.INPUT) {
				parseStageDefinition(pipeline.getInputs(), stageType, definition);
			} 
			else if(stageType == StageType.FILTER) {
				parseStageDefinition(pipeline.getFilteringStage(), stageType, definition);
			}
			else if(stageType == StageType.OUTPUT) {
				parseStageDefinition(pipeline.getOutput(), stageType, definition);
			}

		}
		return pipeline;
	}

	/**
	 * Parse the content of a stage, which is more or less what other people call "statement-block" 
	 * @param stage 
	 * @param stageType
	 * @param definition
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	private void parseStageDefinition(Stage stage, StageType stageType,
			Stage_definitionContext definition) throws NoSuchFieldException, IllegalAccessException {
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
						block.expr = createLogicalExpression(exprCtx.getRuleContext(ParserRuleContext.class, 0)); 
						block.stage = new Stage<>(stage.getPipeline());
						parseStageDefinition(block.stage, stageType, condition.stage_definition(blockIdx));
						filter.blocks.add(block);
					}

					if(condition.stage_definition().size() > condition.logical_expression().size()) {
						filter.elseStage = new Stage<>(stage.getPipeline());
						parseStageDefinition(filter.elseStage, stageType, condition.stage_definition(condition.stage_definition().size() - 1));
					}
					stage.add(filter);
				} else {
					throw new UnsupportedOperationException("No support for else");
				}
			}
			else if(child instanceof Plugin_declarationContext) {
				var pluginDecl = (Plugin_declarationContext)child;
				processPluginDeclaration(stage, stageType, pluginDecl);
			}
		}
	}

	private LogicalExpression createLogicalExpression(ParserRuleContext exprCtx) {
		if(exprCtx instanceof Match_expressionContext) {
			Match_expressionContext match = (Match_expressionContext)exprCtx;
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

		} else if(exprCtx instanceof In_expressionContext){
			In_expressionContext in = (In_expressionContext)exprCtx;
			if(in.rvalue().size() != 2) {
				throw new UnsupportedOperationException("In operator requires two arguments");
			}
			RvalueContext left = in.rvalue().get(0);
			RvalueContext right = in.rvalue().get(1);
			if(in.NOT() != null)
				return new NotInExpression(makeRValue(left), makeRValue(right));
			else
				return new InExpression(makeRValue(left), makeRValue(right));
		} else if(exprCtx instanceof Logical_expressionContext) {
			Logical_expressionContext logicalCtx = (Logical_expressionContext)exprCtx;
			if(logicalCtx.OR() != null) {
				List<Logical_expressionContext> children = logicalCtx.getRuleContexts(Logical_expressionContext.class);
				if(children.size() != 2) {
					throw new UnsupportedOperationException("Can't find two operator for logical or");
				}
				return new OrExpression(createLogicalExpression(children.get(0)), createLogicalExpression(children.get(1)));
			}
			else if(logicalCtx.AND() != null) {
				List<Logical_expressionContext> children = logicalCtx.getRuleContexts(Logical_expressionContext.class);
				if(children.size() != 2) {
					throw new UnsupportedOperationException("Can't find two operator for logical or");
				}
				return new AndExpression(createLogicalExpression(children.get(0)), createLogicalExpression(children.get(1)));
			}
			else if(logicalCtx.LPAREN() != null) {
				List<Logical_expressionContext> children = logicalCtx.getRuleContexts(Logical_expressionContext.class);
				if(children.size() != 1) {
					throw new UnsupportedOperationException("Can't find a single expression inside parentheses");
				}
				Logical_expressionContext exprCtx2 = children.get(0);
				if(exprCtx2.children.size() != 1) {
					throw new UnsupportedOperationException("Can't find a single expression inside parentheses - 2");
				}
				return createLogicalExpression(exprCtx2.getChild(ParserRuleContext.class, 0));
			}
			throw new UnsupportedOperationException("Unrecognized logical operator");
		} else {
			throw new UnsupportedOperationException("No support for this kind of expression");
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
			String[] arrayOfStrings = s.toArray(String[]::new);
			return evt->arrayOfStrings;
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

	private void processPluginDeclaration(Stage stage, StageType stageType, Plugin_declarationContext pluginDecl)
			throws NoSuchFieldException, IllegalAccessException {
		System.err.println(stageType + " plugin " + pluginDecl.IDENTIFIER());
		if(stageType == StageType.INPUT) {
			InputContext ctx = new AbstractInputContext() {
				@Override
				public void processEvent(Event s) {
					stage.getPipeline().processInputEvent(s);
				}
			};
			Input input = pluginFactory.createInputPlugin(pluginDecl.IDENTIFIER().getText()).create(ctx);
			stage.add(input);
			parsePluginConfig(null, input, pluginDecl);
		} else if(stageType == StageType.FILTER) {
			Filter filter = pluginFactory.createFilterPlugin(pluginDecl.IDENTIFIER().getText()).create();
			FilterWrapper wrapper = new FilterWrapper(filter);
			parsePluginConfig(wrapper, filter, pluginDecl);
			stage.add(wrapper);
		} else {
			PipelineComponent.Factory outputPlugin = pluginFactory.createOutputPlugin(pluginDecl.IDENTIFIER().getText());
			if(outputPlugin instanceof Output.Factory) {
				Output plugin = ((Output.Factory)outputPlugin).create();
				stage.add(outputPlugin);
			} else if(outputPlugin instanceof BatchOutput.Factory) {
				BatchAdapter plugin = new BatchAdapter((BatchOutput.Factory)outputPlugin, configuration.batchSize);
				parsePluginConfig(null, plugin.batchOutput, pluginDecl);
				stage.add(plugin);
			} else {
				throw new IllegalArgumentException("Invalid output plugin, must either implement Output or BatchOutput interfaces");
			}
		}
	}

	private void parsePluginConfig(Object wrapper, Object plugin, Plugin_declarationContext pluginDecl) throws NoSuchFieldException, IllegalAccessException {
		Plugin_definitionContext pluginDefinition = pluginDecl.plugin_definition();
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
	}

	private void setObjectParameter(Object plugin, Plugin_attributeContext attribute) throws NoSuchFieldException, IllegalAccessException {
		Plugin_attribute_valueContext value = attribute.plugin_attribute_value();
		System.err.println("  attribute " + attribute.IDENTIFIER() + " is " + value.getText());
		Field field = plugin.getClass().getDeclaredField(attribute.IDENTIFIER().getText());
		field.setAccessible(true);
		if (value.STRING() != null) {
			field.set(plugin, extractStringContent(value.STRING()));
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
