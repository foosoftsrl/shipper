package it.foosoft.shipper.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.logstash.dissect.Dissector;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Param;
import it.foosoft.shipper.plugins.converters.Converters;

/**
 * Implementation of dissect filter based on logstash implementation
 * 
 * @author luca
 *
 */
public class LogstashDissectFilter implements Filter {

	@NotNull
	public Map<String,String> mapping;

	@Param
	public String tag_on_failure;

	@Param(description = "Type conversion (from string)")
	public Map<String, String> convert_datatype = new HashMap<>();

	@Param
	public String[] remove_field = new String[0];

	public List<EventProcessor> converters = new ArrayList<>();

	static class Context {
		Pattern pattern;
		List<String> fields = new ArrayList<>();
	}

	private Map<String,Dissector> contexts = new HashMap<>();

	@Override
	public void process(Event e) {
		for(var pattern: contexts.entrySet()) {
			Object attr = e.getField(pattern.getKey());
			if(!(attr instanceof String)) {
				throw new UnsupportedOperationException("Unsupported field type for " + pattern.getKey());
			}
			String attrString = (String)attr;

			Dissector context = pattern.getValue();
			context.dissect(attrString.getBytes(), e);
			for(var converter: converters) {
				converter.process(e);
			}
		}
		return;
	}

	@Override
	public void start() {
		contexts.clear();
		for(Entry<String, String> a: mapping.entrySet()) {
			var dissector = Dissector.create(a.getValue());
			contexts.put(a.getKey(), dissector);
		}
		prepareConverters();
	}

	private void prepareConverters() {
		converters.clear();
		for(Map.Entry<String, String> entry: convert_datatype.entrySet()) {
			converters.add(Converters.createConverter(entry.getKey(), entry.getValue()));
		}
	}

	@Override
	public void stop() {
	}
}
