package it.foosoft.shipper.core;

import java.util.Objects;

import it.foosoft.shipper.api.RValue;
import it.foosoft.shipper.core.expressions.CompareExpression;

public class NotEqualsExpression extends CompareExpression {

	public NotEqualsExpression(RValue left, RValue right) {
		super(left, right);
	}

	@Override
	protected boolean evaluate(Object left, Object right) {
		return !Objects.equals(left, right);
	}

	@Override
	public String toString() {
		return left + " != " + right;
	}

}
