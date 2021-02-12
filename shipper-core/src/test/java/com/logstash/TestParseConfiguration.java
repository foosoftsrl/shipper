package com.logstash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

import com.logstash.ConfigParser.Stage_declarationContext;

public class TestParseConfiguration {
	@Test
	public void test() throws IOException {
		ConfigLexer lexer = new ConfigLexer(CharStreams.fromStream(getClass().getResourceAsStream("files/config.logstash")));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		var p = new ConfigParser(tokens).config();
		List<Stage_declarationContext> stage_declarations = p.stage_declaration();
		assertEquals(1, stage_declarations.size());
		
		Stage_declarationContext stage_declaration = stage_declarations.get(0);
		TerminalNode input = stage_declaration.INPUT();
		assertNotNull(input);

		assertNotNull(stage_declaration.stage_definition());
		assertEquals(1, stage_declaration.stage_definition().getRuleContexts(ParserRuleContext.class).size());
		
	}
	
    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
	
}
