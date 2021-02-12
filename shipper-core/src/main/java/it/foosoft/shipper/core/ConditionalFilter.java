package it.foosoft.shipper.core;

import java.util.ArrayList;
import java.util.List;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;

public class ConditionalFilter implements Filter {
	
	private LogicalExpression expr;
	private Pipeline pipeline;

	public ConditionalFilter(Pipeline pipeline) {
		this.pipeline = pipeline;
	}
	
	static class ConditionalBlock {
		LogicalExpression expr;
		Stage<Filter> stage;
		void start() {
			stage.start();
		}
		void stop() {
			stage.stop();
		}
	}
	
	/**
	 * if... or else if blocks
	 * else 
	 */
	List<ConditionalBlock> blocks = new ArrayList<>();
	Stage<Filter> elseStage;

	// Don't know if I should return true or false, or whatever...
	@Override
	public boolean process(Event e) {
		if(e.canceled())
			return true;
		for(ConditionalBlock block: blocks) {
			if(block.expr.evaluate(e)) {
				for(var a : block.stage) {
					a.process(e);
					if(e.canceled())
						return true;
				}
				return true;
			}
		}
		return true;
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
