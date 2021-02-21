package it.foosoft.shipper.core.expressions;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.RValue;
import it.foosoft.shipper.core.LogicalExpression;

public abstract class CompareExpression implements LogicalExpression {

	private RValue left;
	private RValue right;

	public CompareExpression(RValue left, RValue right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean evaluate(Event event) {
		return evaluate(left.get(event), right.get(event));
		
	}

	protected abstract boolean evaluate(Object left, Object right);

}
