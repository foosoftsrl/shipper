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
	public Result process(Event e) {
		if(e.canceled())
			return Result.BREAK;
		for(ConditionalBlock<Filter> block: blocks) {
			if(block.expr.evaluate(e)) {
				for(var a : block.stage) {
					if(Result.BREAK == a.process(e) || e.canceled())
						return Result.BREAK;
				}
				return Result.CONTINUE;
			}
		}
		if(elseStage != null) {
			for(var filter: elseStage) {
				filter.process(e);
			}
		}
		return Result.CONTINUE;
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
