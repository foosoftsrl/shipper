package it.foosoft.shipper.plugins.converters;

import it.foosoft.shipper.api.EventProcessor;

public class Converters {
	public static EventProcessor createConverter(String fieldName, String targetFormat) {
		if("integer".equals(targetFormat) || "int".equals(targetFormat)) {
			return new IntegerConverter(fieldName);
		}
		else if("float".equals(targetFormat)) {
			return new FloatConverter(fieldName);
		}
		else {
			throw new UnsupportedOperationException("Can't find a converter to " + targetFormat);
		}
	}
}
