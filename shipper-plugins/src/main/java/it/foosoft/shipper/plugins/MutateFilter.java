package it.foosoft.shipper.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRef;
import it.foosoft.shipper.api.FieldRefBuilder;
import it.foosoft.shipper.api.FilterPlugin;
import it.foosoft.shipper.api.Inject;
import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.plugins.converters.Converters;
import it.foosoft.shipper.plugins.mutate.CopyField;
import it.foosoft.shipper.plugins.mutate.RenameField;

/**
 * Not very complete implementation of logstash's mutate filter
 * 
 * @author luca
 *
 */
public class MutateFilter implements FilterPlugin {

	//@Param
	//public Map<String,String> coerce = new HashMap<>();

	@ConfigurationParm
	public Map<String,String> rename = new HashMap<>();

	//@Param
	//public Map<String,String> update = new HashMap<>();

	//@Param
	//public Map<String,String> replace = new HashMap<>();

	@ConfigurationParm
	public Map<String,String> convert = new HashMap<>();

	//@Param
	//public String[] gsub = new String[0];

	//@Param
	//public String[] uppercase = new String[0];

	//@Param
	//public String[] capitalize = new String[0];

	//@Param
	//public String[] lowercase = new String[0];

	//@Param
	//public String[] strip = new String[0];

	//@Param
	//public String[] remove = new String[0];

	//@Param
	//public String[] split = new String[0];

	//@Param
	//public String[] join = new String[0];

	//@Param
	//public String[] merge = new String[0];

	@ConfigurationParm
	public Map<String,String> copy = new HashMap<>();

	@Inject
	FieldRefBuilder fieldRefBuilder;
	
	private List<EventProcessor> eventProcessors = new ArrayList<>();

	
	public MutateFilter() {
		
	}
	
	@Override
	public boolean process(Event e) {
		for(var processor: eventProcessors)
			processor.process(e);
		return true;
	}

	@Override
	public void start() {
		for(var s: rename.entrySet()) {
			FieldRef sourceField = fieldRefBuilder.createFieldRef(s.getKey());
			FieldRef targetField = fieldRefBuilder.createFieldRef(s.getValue());
			eventProcessors.add(new RenameField(sourceField, targetField));
		}

		for(var convertEntry: convert.entrySet()) {
			String fieldName = convertEntry.getKey();
			String targetFormat = convertEntry.getValue();
			eventProcessors.add(Converters.createConverter(fieldRefBuilder.createFieldRef(fieldName), targetFormat));
		}
		
		for(var copyEntry: copy.entrySet()) {
			FieldRef sourceField = fieldRefBuilder.createFieldRef(copyEntry.getKey());
			FieldRef targetField = fieldRefBuilder.createFieldRef(copyEntry.getValue());
			eventProcessors.add(new CopyField(sourceField, targetField));
		}
/*		
		for(var s: gsub) {
			throw new UnsupportedOperationException("No gsub support");
		}
*/
	}

	@Override
	public void stop() {
	}
}
