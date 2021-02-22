package it.foosoft.shipper.core;

import java.util.HashMap;
import java.util.Map;

import it.foosoft.shipper.api.Bag;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.InputContext;
import it.foosoft.shipper.api.ConfigurationParm;

/**
 * An input decorator which implements common logic (add_field, add_tag...) 
 * 
 * @author luca
 *
 */
public class InputWrapper implements Input, InputContext {

	@ConfigurationParm
	public Map<String,String> add_field = new HashMap<>();

	@ConfigurationParm
	public String[] add_tag = new String[0];

	@ConfigurationParm
	public String type = null;

	@ConfigurationParm
	public String id;
	
	public final Input wrapped;

	private Bag stopStatus;

	private Bag startStatus;

	private Pipeline pipeline;

	public InputWrapper(Pipeline pipeline, Input.Factory inputPlugin) {
		this.wrapped = inputPlugin.create(this);
		this.pipeline = pipeline;
	}

	public String getId() {
		return id;
	}

	@Override
	public void processEvent(Event e) {
		for(String tag: add_tag)
			e.addTag(tag);
		for(var entry: add_field.entrySet())
			e.setField(entry.getKey(), entry.getValue());
		pipeline.processInputEvent(e);
	}

	@Override
	public Event createEvent() {
		return new EventImpl();
	}

	@Override
	public Bag createBag() {
		return new BagImpl();
	}

	@Override
	public void setStopStatus(Bag bag) {
		this.stopStatus = bag;
	}

	@Override
	public Bag getStartStatus() {
		return startStatus;
	}

	@Override
	public void start() {
		wrapped.start();
	}

	@Override
	public void stop() {
		wrapped.stop();
	}

	public Bag getStopStatus() {
		return stopStatus;
	}

	public void setStartStatus(Bag props) {
		this.startStatus = props;
	}
}
