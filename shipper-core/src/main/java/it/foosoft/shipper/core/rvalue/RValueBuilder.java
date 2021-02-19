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
}
