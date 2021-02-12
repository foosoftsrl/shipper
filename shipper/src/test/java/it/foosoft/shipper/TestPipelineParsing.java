package it.foosoft.shipper;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

import com.logstash.ConfigLexer;
import com.logstash.ConfigListener;
import com.logstash.ConfigParser;
import com.logstash.ConfigParser.ArrayContext;
import com.logstash.ConfigParser.Array_elementContext;
import com.logstash.ConfigParser.Compare_expressionContext;
import com.logstash.ConfigParser.ConfigContext;
import com.logstash.ConfigParser.FieldrefContext;
import com.logstash.ConfigParser.Fieldref_elementContext;
import com.logstash.ConfigParser.HashContext;
import com.logstash.ConfigParser.Hash_elementContext;
import com.logstash.ConfigParser.In_expressionContext;
import com.logstash.ConfigParser.Logical_expressionContext;
import com.logstash.ConfigParser.Match_expressionContext;
import com.logstash.ConfigParser.Negative_expressionContext;
import com.logstash.ConfigParser.Plugin_attributeContext;
import com.logstash.ConfigParser.Plugin_attribute_valueContext;
import com.logstash.ConfigParser.Plugin_declarationContext;
import com.logstash.ConfigParser.Plugin_definitionContext;
import com.logstash.ConfigParser.RvalueContext;
import com.logstash.ConfigParser.Stage_conditionContext;
import com.logstash.ConfigParser.Stage_declarationContext;
import com.logstash.ConfigParser.Stage_definitionContext;

import it.foosoft.shipper.core.Pipeline;
import it.foosoft.shipper.core.PipelineBuilder;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.plugins.DefaultPluginFactory;

public class TestPipelineParsing {

	@Test
	public void testComplexPipeline() throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Object pipeline = PipelineBuilder.parse(DefaultPluginFactory.INSTANCE, Configuration.MINIMAL, TestPipelineParsing.class.getResource("files/logstash.conf"));
		walk();
	}
	
	@Test
	public void testConditions() throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Pipeline pipeline = PipelineBuilder.parse(DefaultPluginFactory.INSTANCE, Configuration.MINIMAL, TestPipelineParsing.class.getClassLoader().getResource("it/foosoft/shipper/files/conditions.conf"));
		assertEquals(1, pipeline.getFilteringStage().size());
	}

	private static void walk() throws IOException {
		ConfigLexer lexer = new ConfigLexer(CharStreams.fromStream(TestPipelineParsing.class.getResourceAsStream("files/logstash.conf")));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ConfigParser p = new ConfigParser(tokens);
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new ConfigListener() {
			@Override
			public void visitTerminal(TerminalNode node) {
				System.err.println("visitTerminal " + node);
			}

			@Override
			public void visitErrorNode(ErrorNode node) {
				System.err.println("visitErrorNode");
			}

			@Override
			public void enterEveryRule(ParserRuleContext ctx) {
				System.err.println("enterEveryRule");
			}

			@Override
			public void exitEveryRule(ParserRuleContext ctx) {
								System.err.println("exitEveryRule");
				
			}

			@Override
			public void enterConfig(ConfigContext ctx) {
								System.err.println("enterConfig");
				
			}

			@Override
			public void exitConfig(ConfigContext ctx) {
								System.err.println("exitConfig");
				
			}

			@Override
			public void enterStage_declaration(Stage_declarationContext ctx) {
								System.err.println("enterStage_declaration");
				
			}

			@Override
			public void exitStage_declaration(Stage_declarationContext ctx) {
								System.err.println("exitStage_declaration");
				
			}

			@Override
			public void enterStage_definition(Stage_definitionContext ctx) {
								System.err.println("enterStage_definition");
				
			}

			@Override
			public void exitStage_definition(Stage_definitionContext ctx) {
								System.err.println("exitStage_definition");
				
			}

			@Override
			public void enterPlugin_declaration(Plugin_declarationContext ctx) {
								System.err.println("enterPlugin_declaration");
				
			}

			@Override
			public void exitPlugin_declaration(Plugin_declarationContext ctx) {
								System.err.println("exitPlugin_declaration");
				
			}

			@Override
			public void enterPlugin_definition(Plugin_definitionContext ctx) {
								System.err.println("enterPlugin_definition");
				
			}

			@Override
			public void exitPlugin_definition(Plugin_definitionContext ctx) {
								System.err.println("exitPlugin_definition");
				
			}

			@Override
			public void enterPlugin_attribute(Plugin_attributeContext ctx) {
								System.err.println("enterPlugin_attribute");
				
			}

			@Override
			public void exitPlugin_attribute(Plugin_attributeContext ctx) {
								System.err.println("exitPlugin_attribute");
				
			}

			@Override
			public void enterPlugin_attribute_value(Plugin_attribute_valueContext ctx) {
				System.err.println("enterPlugin_attribute_value" + ctx);
				
			}

			@Override
			public void exitPlugin_attribute_value(Plugin_attribute_valueContext ctx) {
								System.err.println("exitPlugin_attribute_value");
				
			}

			@Override
			public void enterStage_condition(Stage_conditionContext ctx) {
								System.err.println("enterStage_condition");
				
			}

			@Override
			public void exitStage_condition(Stage_conditionContext ctx) {
								System.err.println("exitStage_condition");
				
			}

			@Override
			public void enterLogical_expression(Logical_expressionContext ctx) {
								System.err.println("enterLogical_expression");
				
			}

			@Override
			public void exitLogical_expression(Logical_expressionContext ctx) {
								System.err.println("exitLogical_expression");
				
			}

			@Override
			public void enterNegative_expression(Negative_expressionContext ctx) {
								System.err.println("enterNegative_expression");
				
			}

			@Override
			public void exitNegative_expression(Negative_expressionContext ctx) {
								System.err.println("exitNegative_expression");
				
			}

			@Override
			public void enterCompare_expression(Compare_expressionContext ctx) {
								System.err.println("enterCompare_expression");
				
			}

			@Override
			public void exitCompare_expression(Compare_expressionContext ctx) {
								System.err.println("enterCompare_expression");
				
			}

			@Override
			public void enterIn_expression(In_expressionContext ctx) {
								System.err.println("enterIn_expression");
				
			}

			@Override
			public void exitIn_expression(In_expressionContext ctx) {
								System.err.println("exitIn_expression");
				
			}

			@Override
			public void enterMatch_expression(Match_expressionContext ctx) {
								System.err.println("enterMatch_expression");
				
			}

			@Override
			public void exitMatch_expression(Match_expressionContext ctx) {
								System.err.println("exitMatch_expression");
				
			}

			@Override
			public void enterRvalue(RvalueContext ctx) {
								System.err.println("enterRvalue");
				
			}

			@Override
			public void exitRvalue(RvalueContext ctx) {
								System.err.println("exitRvalue");
				
			}

			@Override
			public void enterFieldref(FieldrefContext ctx) {
								System.err.println("enterFieldref");
				
			}

			@Override
			public void exitFieldref(FieldrefContext ctx) {
								System.err.println("exitFieldref");
				
			}

			@Override
			public void enterFieldref_element(Fieldref_elementContext ctx) {
								System.err.println("enterFieldref_element");
				
			}

			@Override
			public void exitFieldref_element(Fieldref_elementContext ctx) {
								System.err.println("exitFieldref_element");
				
			}

			@Override
			public void enterArray(ArrayContext ctx) {
								System.err.println("enterArray");
				
			}

			@Override
			public void exitArray(ArrayContext ctx) {
								System.err.println("exitArray");
				
			}

			@Override
			public void enterArray_element(Array_elementContext ctx) {
								System.err.println("enterArray_element");
				
			}

			@Override
			public void exitArray_element(Array_elementContext ctx) {
								System.err.println("exitArray_element");
				
			}

			@Override
			public void enterHash(HashContext ctx) {
								System.err.println("enterHash");
				
			}

			@Override
			public void exitHash(HashContext ctx) {
								System.err.println("exitHash");
				
			}

			@Override
			public void enterHash_element(Hash_elementContext ctx) {
								System.err.println("enterHash_element");
				
			}

			@Override
			public void exitHash_element(Hash_elementContext ctx) {
								System.err.println("exitHash_element");
				
			}
			
		}, p.config());
	}
}
