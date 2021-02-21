package it.foosoft.shipper.core.expressions;

import java.util.Objects;

import it.foosoft.shipper.api.RValue;

public class EqualsExpression extends CompareExpression {

	public EqualsExpression(RValue left, RValue right) {
		super(left, right);
	}

	@Override
	protected boolean evaluate(Object left, Object right) {
		return Objects.equals(left, right);
	}

}
