package it.foosoft.shipper.core;

import org.antlr.v4.runtime.ParserRuleContext;

public class InvalidPipelineException extends RuntimeException {
	private int lineNumber;

	public InvalidPipelineException(String source, ParserRuleContext parserContext, String message) {
		super("At " + source + ":" + parserContext.getStart().getLine() + " : " + message);
		this.lineNumber = parserContext.getStart().getLine();
	}

	public int getLineNumber() {
		return lineNumber;
	}
}
