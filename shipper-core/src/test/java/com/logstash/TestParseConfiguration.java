package com.logstash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

public class TestParseConfiguration {
	@Test
	public void test() throws IOException {
		ConfigLexer lexer = new ConfigLexer(CharStreams.fromStream(getClass().getResourceAsStream("files/config.logstash")));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ConfigParser p = new ConfigParser(tokens);
		assertEquals(1, p.config().stage_declaration().size());
		TerminalNode input = p.config().stage_declaration().get(0).INPUT();
		assertNotNull(input);
		assertEquals(1, input.getChildCount());
		
	}
}
