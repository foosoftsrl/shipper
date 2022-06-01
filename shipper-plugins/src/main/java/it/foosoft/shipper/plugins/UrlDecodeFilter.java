package it.foosoft.shipper.plugins;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FilterPlugin;

public class UrlDecodeFilter implements FilterPlugin {
	private static final Logger LOG = LoggerFactory.getLogger(UrlDecodeFilter.class);
	
	@NotNull
	@ConfigurationParm(description="The field to decode")
	String field;
	
	@Override
	public boolean process(Event e) {
		String s = e.getFieldAsString(field);
		if(s == null) {
			LOG.warn("Field {} is not a string", field);
			return false;
		}
		// don't know what charset to use
		e.setField(field, URLDecoder.decode(s, StandardCharsets.UTF_8));
		return true;
	}

	@Override
	public void start() {
		// nothing to do
	}

	@Override
	public void stop() {
		// nothing to do
	}

	
}
