package it.foosoft.shipper.core;

import it.foosoft.shipper.api.Event;

/**
 * Exit from the current stage
 * 
 * @author luca
 */
public class ExitStatement implements Filter {

	@Override
	public Result process(Event e) {
		return Result.BREAK;
	}

}
