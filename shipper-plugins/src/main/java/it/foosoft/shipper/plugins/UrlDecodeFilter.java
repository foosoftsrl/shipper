package it.foosoft.shipper.plugins;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import org.apache.commons.codec.Charsets;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Param;

public class UrlDecodeFilter implements Filter {

	@NotNull
	@Param(description="The field to decode")
	String field;
	
	URLDecoder urlDecoder = new URLDecoder();
	
	@Override
	public void process(Event e) {
		String s = e.getFieldAsString(field);
		if(s != null) {
			// don't know what charset to use
			e.setField(field, urlDecoder.decode(s, StandardCharsets.UTF_8));
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	
}
