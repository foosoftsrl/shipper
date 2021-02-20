package it.foosoft.shipper.core.rvalue;

import java.util.Arrays;

import it.foosoft.shipper.api.RValue;

public class RValueBuilder {

	public static RValue makeFieldRefRValue(String... identifiers) {
		if(identifiers.length == 0) {
			throw new IllegalArgumentException("No identifiers in fieldRef expression");
		}
		else if("@metadata".equals(identifiers[0])) {
			return new MetadataRefRvalue(Arrays.asList(identifiers).subList(1, identifiers.length));
		}
		else if(identifiers.length == 1) {
			// Special (faster) case for single identifier
			return new SingleFieldRefRValue(identifiers[0]);
		} 
		else {
			return new ComplexFieldRefRvalue(identifiers);
		}
	}
	
	public static RValue makeFieldRefRValueFromString(String expr) {
		// split on ][, this is needed for nested
		String[] fields = expr.split("\\]\\[");
		for(int i = 0; i < fields.length; i++) {
			// simply drop ']' and '[' we don't need them 
			String field = fields[i].replace("[", "");			
			field = field.replace("]", "");
			fields[i] = field;
		}
		return makeFieldRefRValue(fields);
	}
}
