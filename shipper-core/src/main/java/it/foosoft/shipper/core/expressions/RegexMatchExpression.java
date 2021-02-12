package it.foosoft.shipper.core.expressions;

import java.util.regex.Pattern;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.RValue;
import it.foosoft.shipper.core.LogicalExpression;

public class RegexMatchExpression implements LogicalExpression {

	protected final RValue rvalue;
	protected final String regexp;
	protected final Pattern pattern;

	public RegexMatchExpression(RValue rValue, String regexp) {
		this.regexp = regexp;
		this.rvalue = rValue;
		pattern = Pattern.compile(regexp);
	}

	@Override
	public boolean evaluate(Event event) {
		Object fieldValue = rvalue.get(event);
		if(!(fieldValue instanceof String))
			return false;
		return pattern.matcher((String)fieldValue).find();
	}

	public String getRegexp() {
		return regexp;
	}
	
	@Override
	public String toString() {
		return rvalue + "=~" + regexp;
	}
	
}
