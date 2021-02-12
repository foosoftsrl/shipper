package it.foosoft.shipper.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Param;
import it.foosoft.shipper.core.modifiers.AddField;
import it.foosoft.shipper.core.modifiers.AddTag;
import it.foosoft.shipper.core.modifiers.RemoveField;

/**
 * A filter decorator which implements common logic (add_field, add_tag...) 
 * 
 * @author luca
 *
 */
public class FilterWrapper implements Filter {

	@Param
	public String id;

	@Param
	public String[] remove_field = new String[0];

	@Param
	public Map<String,String> add_field = new HashMap<>();

	@Param
	public String[] add_tag = new String[0];

	@NotNull
	private final Filter inner;

	private List<EventProcessor> eventProcessors = new ArrayList<>();
	
	public FilterWrapper(@NotNull Filter inner) {
		this.inner = inner;
	}


	@Override
	public void start() {
		inner.start();

		for(var addFieldEntry: add_field.entrySet()) {
			eventProcessors.add(new AddField(addFieldEntry.getKey(), addFieldEntry.getValue()));
		}

		for(String removeFieldEntry: remove_field) {
			eventProcessors.add(new RemoveField(removeFieldEntry));
		}

		for(String addTag: add_tag) {
			eventProcessors.add(new AddTag(addTag));
		}
	}

	@Override
	public void stop() {
		inner.stop();
	}

	@Override
	public boolean process(Event e) {
		if(inner.process(e)) {
			for(var processor: eventProcessors) {
				processor.process(e);
			}
		}
		return true;
	}

	public String getId() {
		return id;
	}


	public Filter getInner() {
		return inner;
	}

}
