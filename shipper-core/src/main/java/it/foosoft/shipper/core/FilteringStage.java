package it.foosoft.shipper.core;

import it.foosoft.shipper.api.Event;

public class FilteringStage extends Stage<Filter> implements Filter {
	@Override
	public Result process(Event e) {
		for(var filter: this) {
			if(Result.BREAK == filter.process(e) || e.canceled())
				return Result.BREAK;
		}
		return Result.CONTINUE;
	}
	
}
