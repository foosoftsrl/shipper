package it.foosoft.shipper.core;

import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.Param;

public class InputWrapper {
	@Param
	public String id;
	
	public Input wrapped;

	public InputWrapper(Input input) {
		this.wrapped = input;
	}

	public String getId() {
		return id;
	}

}
