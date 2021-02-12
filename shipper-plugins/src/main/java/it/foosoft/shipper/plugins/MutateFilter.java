package it.foosoft.shipper.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Param;
import it.foosoft.shipper.plugins.converters.Converters;
import it.foosoft.shipper.plugins.mutate.CopyField;

/**
 * Not very complete implementation of logstash's mutate filter
 * 
 * @author luca
 *
 */
public class MutateFilter implements Filter {
	@Param
	public String[] gsub = new String[0];

	@Param
	public Map<String,String> convert = new HashMap<>();

	@Param
	public Map<String,String> copy = new HashMap<>();

	private List<EventProcessor> eventProcessors = new ArrayList<>();
	
	
	@Override
	public boolean process(Event e) {
		for(var processor: eventProcessors)
			processor.process(e);
		return true;
	}

	@Override
	public void start() {
		for(var convertEntry: convert.entrySet()) {
			String fieldName = convertEntry.getKey();
			String targetFormat = convertEntry.getValue();
			eventProcessors.add(Converters.createConverter(fieldName, targetFormat));
		}
		
		for(var copyEntry: copy.entrySet()) {
			String sourceField = copyEntry.getKey();
			String targetField = copyEntry.getValue();
			eventProcessors.add(new CopyField(sourceField, targetField));
		}
		
		for(var s: gsub) {
			throw new UnsupportedOperationException("No gsub support");
		}

	}

	@Override
	public void stop() {
	}
}
