package it.foosoft.shipper.core;

import it.foosoft.shipper.api.Event;

public interface LogicalExpression {
	boolean evaluate(Event event);
}
