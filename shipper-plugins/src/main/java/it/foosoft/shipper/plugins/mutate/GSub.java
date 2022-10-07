package it.foosoft.shipper.plugins.mutate;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRef;

public class GSub implements EventProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(GSub.class);
	private Pattern pattern;
	private String with;
	private FieldRef field;

	public GSub(FieldRef fieldRef, String what, String with) {
		this.field = fieldRef;
		this.with = with;
		pattern = java.util.regex.Pattern.compile(what);
	}

	@Override
	public void process(Event e) {
		Object obj = field.get(e);
		if(obj instanceof String) {
			String str = (String)obj;
			String out = pattern.matcher(str).replaceAll(with);
			field.set(e, out);
		} else {
			LOG.info("trying to gsub a non string");
		}
	}

}
