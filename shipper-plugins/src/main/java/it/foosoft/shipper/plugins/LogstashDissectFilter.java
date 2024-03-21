package it.foosoft.shipper.plugins;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import it.foosoft.shipper.plugins.converters.Converter;
import org.logstash.dissect.DissectResult;
import org.logstash.dissect.Dissector;
import org.slf4j.Logger;

import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRefBuilder;
import it.foosoft.shipper.api.FilterPlugin;
import it.foosoft.shipper.api.Inject;
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
	public Map<String,String> mapping = new LinkedHashMap<>();

	@ConfigurationParm
	public String[] tag_on_failure = new String[] {"_dissectfailure"};

	@ConfigurationParm(description = "Type conversion (from string)")
	public Map<String, String> convert_datatype = new LinkedHashMap<>();

	@Inject
	public FieldRefBuilder fieldRefBuilder;
	
	public Map<String, Converter> converters = new HashMap<String, Converter>();

	static class Context {
		Pattern pattern;
		List<String> fields = new ArrayList<>();
	}

	private Map<String,Dissector> contexts = new LinkedHashMap<>();

	@Override
	public boolean process(Event evt) {
		boolean successful = true;
		for(var pattern: contexts.entrySet()) {
			Object attr = evt.getField(pattern.getKey());
			if(attr == null) {
				LOG.warn("Field {} is missing in {}", pattern.getKey(), evt.toString());
				evt.addTags(tag_on_failure);
				return false;
			}
			if(!(attr instanceof String)) {
				LOG.warn("Unsupported field type for {}", pattern.getKey());
				return false;
			}

			String attrString = (String)attr;
			Dissector context = pattern.getValue();
			DissectResult result = context.dissect(attrString.getBytes(), evt);
			if(!result.matched()) {
				evt.addTags(tag_on_failure);
				return false;
			}
		}
		for(var converter: converters.entrySet()) {
			try {
				converter.getValue().process(evt);
			} catch(RuntimeException e) {
				evt.addTag(String.format("_dataconversionuncoercible_%s", converter.getKey()));
				LOG.warn("Failed coercing field '{}' to {}",
						converter.getKey(), converter.getValue().targetType());
				return false;
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
			converters.put(entry.getKey(), Converters.createConverter(fieldRefBuilder.createFieldRef(entry.getKey()), entry.getValue()));
		}
	}

	@Override
	public void stop() {
	}
}
