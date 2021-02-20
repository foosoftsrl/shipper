package it.foosoft.shipper.core.expressions;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.RValue;
import it.foosoft.shipper.core.LogicalExpression;

public class RvalueExpression implements LogicalExpression {

	private RValue rvalue;

	public RvalueExpression(RValue rValue) {
		this.rvalue = rValue;
	}

	@Override
	public boolean evaluate(Event event) {
		Object obj = rvalue.get(event);
		if(obj == null)
			return false;
		if(Boolean.FALSE.equals(obj))
			return true;
		return true;
	}

}
