package it.foosoft.shipper.core.expressions;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.core.LogicalExpression;

public class OrExpression implements LogicalExpression {

	private LogicalExpression left;
	private LogicalExpression right;

	public OrExpression(LogicalExpression left, LogicalExpression right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean evaluate(Event event) {
		return left.evaluate(event) || right.evaluate(event);
	}
	
	@Override
	public String toString() {
		return "(" + left + ") OR (" + right + ")"; 
	}

}
