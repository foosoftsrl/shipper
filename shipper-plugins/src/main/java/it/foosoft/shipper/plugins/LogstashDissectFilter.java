package it.foosoft.shipper.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.logstash.dissect.DissectResult;
import org.logstash.dissect.Dissector;
import org.slf4j.Logger;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRefBuilder;
import it.foosoft.shipper.api.FilterPlugin;
import it.foosoft.shipper.api.Inject;
import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.plugins.converters.Converters;

/**
 * Implementation of dissect filter based on logstash implementation
 * 
 * @author luca
 *
 */
public class LogstashDissectFilter implements FilterPlugin {

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(LogstashDissectFilter.class);
	
	@NotNull
	public Map<String,String> mapping;

	@ConfigurationParm
	public String[] tag_on_failure = new String[] {"_dissectfailure"};

	@ConfigurationParm(description = "Type conversion (from string)")
	public Map<String, String> convert_datatype = new HashMap<>();

	@Inject
	public FieldRefBuilder fieldRefBuilder;
	
	public List<EventProcessor> converters = new ArrayList<>();

	static class Context {
		Pattern pattern;
		List<String> fields = new ArrayList<>();
	}

	private Map<String,Dissector> contexts = new HashMap<>();

	@Override
	public boolean process(Event e) {
		boolean successful = true;
		for(var pattern: contexts.entrySet()) {
			Object attr = e.getField(pattern.getKey());
			if(!(attr instanceof String)) {
				LOG.warn("Unsupported field type for " + pattern.getKey());
				return false;
			}

			String attrString = (String)attr;
			Dissector context = pattern.getValue();
			DissectResult result = context.dissect(attrString.getBytes(), e);
			if(!result.matched()) {
				e.addTags(tag_on_failure);
				return false;
			}
			for(var converter: converters) {
				converter.process(e);
			}
		}
		return successful;
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
			converters.add(Converters.createConverter(fieldRefBuilder.createFieldRef(entry.getKey()), entry.getValue()));
		}
	}

	@Override
	public void stop() {
	}
}
