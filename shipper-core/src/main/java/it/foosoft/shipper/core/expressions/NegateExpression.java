package it.foosoft.shipper.core.expressions;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.core.LogicalExpression;

public class NegateExpression implements LogicalExpression {

	private LogicalExpression expression;

	public NegateExpression(LogicalExpression expression) {
		this.expression = expression;
	}

	@Override
	public boolean evaluate(Event event) {
		return !expression.evaluate(event);
	}

}
