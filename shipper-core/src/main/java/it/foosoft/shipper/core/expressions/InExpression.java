package it.foosoft.shipper.core.expressions;

import java.util.Collection;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.RValue;
import it.foosoft.shipper.core.LogicalExpression;

public class InExpression implements LogicalExpression {

	protected RValue left;
	protected RValue right;

	public InExpression(RValue left, RValue right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean evaluate(Event event) {
		Object leftObj = left.get(event);
		Object rightObj = right.get(event);
		if(!(leftObj instanceof String))
			return false;
		if(!(rightObj instanceof Collection))
			return false;
		String leftStr = (String)leftObj;
		Collection rightStr = (Collection)rightObj;
		for(Object s: rightStr) {
			if(s.equals(leftStr))
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return left + " IN " + right;
	}
}
