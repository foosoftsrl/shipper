package it.foosoft.shipper.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FieldRef;
import it.foosoft.shipper.api.FieldRefBuilder;
import it.foosoft.shipper.api.FilterPlugin;
import it.foosoft.shipper.api.Inject;
import it.foosoft.shipper.api.StringInterpolatorBuilder;
import it.foosoft.shipper.plugins.converters.Converters;
import it.foosoft.shipper.plugins.mutate.CopyField;
import it.foosoft.shipper.plugins.mutate.GSub;
import it.foosoft.shipper.plugins.mutate.RenameField;
import it.foosoft.shipper.plugins.mutate.ReplaceField;
import it.foosoft.shipper.plugins.mutate.SplitField;

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

	@ConfigurationParm
	public Map<String,String> replace = new HashMap<>();

	@ConfigurationParm
	public Map<String,String> convert = new HashMap<>();

	@ConfigurationParm
	public String[] gsub = new String[0];

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

	@ConfigurationParm
	public Map<String,String> split = new HashMap<>();

	//@Param
	//public String[] join = new String[0];

	//@Param
	//public String[] merge = new String[0];

	@ConfigurationParm
	public Map<String,String> copy = new HashMap<>();

	@Inject
	FieldRefBuilder fieldRefBuilder;
	
	@Inject 
	StringInterpolatorBuilder stringInterpolatorBuilder;
	
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
		//coerce (not implemented)

		//rename
		for(var s: rename.entrySet()) {
			FieldRef sourceField = fieldRefBuilder.createFieldRef(s.getKey());
			FieldRef targetField = fieldRefBuilder.createFieldRef(s.getValue());
			eventProcessors.add(new RenameField(sourceField, targetField));
		}
		//update (not implemented)
		
		//replace (not implemented)
		for(var s: replace.entrySet()) {
			FieldRef sourceField = fieldRefBuilder.createFieldRef(s.getKey());
			eventProcessors.add(new ReplaceField(sourceField, stringInterpolatorBuilder.createStringInterpolator(s.getValue())));
		}

		//convert 
		for(var convertEntry: convert.entrySet()) {
			String fieldName = convertEntry.getKey();
			String targetFormat = convertEntry.getValue();
			eventProcessors.add(Converters.createConverter(fieldRefBuilder.createFieldRef(fieldName), targetFormat));
		}
		
		//gsub
		if(gsub.length % 3 != 0) {
			throw new IllegalStateException("gsub array length must be multiple of 3");
		}

		for(int i = 0; i < gsub.length; i+= 3) {
			FieldRef fieldRef = fieldRefBuilder.createFieldRef(gsub[i]);
			eventProcessors.add(new GSub(fieldRef, gsub[i + 1], gsub[i + 2]));
		}
		//uppercase (not implemented)
		//capitalize (not implemented)
		//lowercase (not implemented)
		//strip (not implemented)
		//split 
		for(var splitEntry: split.entrySet()) {
			FieldRef sourceField = fieldRefBuilder.createFieldRef(splitEntry.getKey());
			eventProcessors.add(new SplitField(sourceField, splitEntry.getValue()));
		}

		//join (not implemented)
		//merge (not implemented)
		
		for(var copyEntry: copy.entrySet()) {
			FieldRef sourceField = fieldRefBuilder.createFieldRef(copyEntry.getKey());
			FieldRef targetField = fieldRefBuilder.createFieldRef(copyEntry.getValue());
			eventProcessors.add(new CopyField(sourceField, targetField));
		}
	}

	@Override
	public void stop() {
	}
}
