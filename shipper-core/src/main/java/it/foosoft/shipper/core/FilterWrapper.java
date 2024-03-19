package it.foosoft.shipper.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.FilterPlugin;
import it.foosoft.shipper.api.PipelineComponent;
import it.foosoft.shipper.api.ConfigurationParm;
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

	@ConfigurationParm
	public String id;

	@ConfigurationParm
	public boolean dump = false;

	@ConfigurationParm
	public String[] remove_field = new String[0];

	@ConfigurationParm
	public Map<String,Object> add_field = new HashMap<>();

	@ConfigurationParm
	public String[] add_tag = new String[0];

	@NotNull
	private final FilterPlugin inner;

	private List<EventProcessor> eventProcessors = new ArrayList<>();
	
	public FilterWrapper(@NotNull FilterPlugin inner) {
		this.inner = inner;
	}


	@Override
	public void start() {
		inner.start();

		FieldRefBuilderImpl fieldRefBuilder = new FieldRefBuilderImpl(); 
		for(var entry: add_field.entrySet()) {
			Object value = entry.getValue();
			eventProcessors.add(AddField.create(fieldRefBuilder.createFieldRef(entry.getKey()), value));
		}

		for(String fieldEntry: remove_field) {
			eventProcessors.add(new RemoveField(fieldRefBuilder.createFieldRef(fieldEntry)));
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
	public Result process(Event e) {
		if(dump) {
            try {
                System.err.println(new ObjectMapper().writeValueAsString(e));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
		if(inner.process(e)) {
			for(var processor: eventProcessors) {
				processor.process(e);
			}
		}
		if(dump) {
			try {
				System.err.println(new ObjectMapper().writeValueAsString(e));
			} catch (JsonProcessingException ex) {
				throw new RuntimeException(ex);
			}
		}
		return Result.CONTINUE;
	}

	public String getId() {
		return id;
	}


	public FilterPlugin getInner() {
		return inner;
	}

	@Override
	public void traverse(Consumer<PipelineComponent> consumer) {
		consumer.accept(this);
		consumer.accept(inner);
	}
}
