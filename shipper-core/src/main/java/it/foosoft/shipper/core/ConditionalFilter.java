package it.foosoft.shipper.core;

import java.util.ArrayList;
import java.util.List;

import it.foosoft.shipper.api.Event;

public class ConditionalFilter implements Filter {
	
	public ConditionalFilter() {
	}
	
	/**
	 * if... or else if blocks
	 * else 
	 */
	List<ConditionalBlock<Filter>> blocks = new ArrayList<>();
	Stage<Filter> elseStage;

	@Override
	public void process(Event e) {
		if(e.canceled())
			return;
		for(ConditionalBlock<Filter> block: blocks) {
			if(block.expr.evaluate(e)) {
				for(var a : block.stage) {
					a.process(e);
					if(e.canceled())
						return;
				}
				return;
			}
		}
		if(elseStage != null) {
			for(var filter: elseStage) {
				filter.process(e);
			}
		}
		return;
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
