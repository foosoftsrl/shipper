package it.foosoft.shipper.core;

import java.util.Arrays;

import it.foosoft.shipper.api.FieldRef;
import it.foosoft.shipper.api.FieldRefBuilder;
import it.foosoft.shipper.core.rvalue.MetadataRefRvalue;
import it.foosoft.shipper.core.rvalue.TimestampRvalue;

public class FieldRefBuilderImpl implements FieldRefBuilder {

	@Override
	public FieldRef createFieldRef(String expr) {
		String[] fields = expr.split("\\]\\[");
		for(int i = 0; i < fields.length; i++) {
			// simply drop ']' and '[' we don't need them 
			String field = fields[i].replace("[", "");			
			field = field.replace("]", "");
			fields[i] = field;
		}
		if("@metadata".equals(fields[0])) {
			return new MetadataRefRvalue(Arrays.asList(fields).subList(1, fields.length));
		}
		if("@timestamp".equals(fields[0])) {
			return new TimestampRvalue();
		}
		return new FieldRefImpl(expr);
	}

}
