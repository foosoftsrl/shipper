package it.foosoft.shipper.core;

import java.util.ArrayList;
import java.util.List;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Output;

public class ConditionalOutput implements Output {
	
	public ConditionalOutput() {
	}
	
	/**
	 * if... or else if blocks
	 * else 
	 */
	List<ConditionalBlock<Output>> blocks = new ArrayList<>();
	Stage<Output> elseStage;

	// Don't know if I should return true or false, or whatever...
	@Override
	public void process(Event e) {
		if(e.canceled())
			return;
		for(ConditionalBlock<Output> block: blocks) {
			if(block.expr.evaluate(e)) {
				for(var a : block.stage) {
					a.process(e);
				}
				return;
			}
		}
		if(elseStage != null) {
			for(var output: elseStage) {
				output.process(e);
			}
		}
	}

	@Override
	public void start() {
		blocks.forEach(ConditionalBlock::start);
		if(elseStage != null)
			elseStage.start();
	}

	@Override
	public void stop() {
		blocks.forEach(ConditionalBlock::stop);
		if(elseStage != null)
			elseStage.stop();
	}

}
