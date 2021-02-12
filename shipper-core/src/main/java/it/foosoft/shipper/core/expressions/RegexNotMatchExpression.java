package it.foosoft.shipper.core.expressions;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.RValue;

public class RegexNotMatchExpression extends RegexMatchExpression {

	public RegexNotMatchExpression(RValue rValue, String regexp) {
		super(rValue, regexp);
	}

	@Override
	public boolean evaluate(Event event) {
		return !super.evaluate(event);
	}
	
	@Override
	public String toString() {
		return rvalue + "!~" + regexp;
	}

}
