package it.foosoft.shipper.api;

public interface Input extends Plugin {
	public interface Factory {
		Input create(InputContext ctx);
	}
}
