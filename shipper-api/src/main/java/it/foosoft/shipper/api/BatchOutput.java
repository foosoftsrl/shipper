package it.foosoft.shipper.api;

public interface BatchOutput extends Plugin {
	public interface Factory extends Plugin.Factory {
		BatchOutput create(OutputContext ctx);
	}
}
