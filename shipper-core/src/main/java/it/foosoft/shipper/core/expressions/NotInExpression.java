package it.foosoft.shipper.core.expressions;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.RValue;

public class NotInExpression extends InExpression {

	public NotInExpression(RValue left, RValue right) {
		super(left, right);
	}

	@Override
	public boolean evaluate(Event event) {
		return !super.evaluate(event);
	}

	@Override
	public String toString() {
		return left + " NOT IN " + right;
	}

}
