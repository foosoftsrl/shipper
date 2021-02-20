package it.foosoft.shipper.plugins.converters;

import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRef;

public class Converters {
	public static EventProcessor createConverter(FieldRef fieldRef, String targetFormat) {
		if("integer".equals(targetFormat) || "int".equals(targetFormat)) {
			return new IntegerConverter(fieldRef);
		}
		else if("float".equals(targetFormat)) {
			return new FloatConverter(fieldRef);
		}
		else {
			throw new UnsupportedOperationException("Can't find a converter to " + targetFormat);
		}
	}
}
