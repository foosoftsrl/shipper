package it.foosoft.shipper.api;

/**
 * Interface for batch-oriented outputs (should be used by most plugins) 
 * @author luca
 *
 */
public interface BatchOutput extends PipelineComponent {
	public interface Factory extends PipelineComponent.Factory {
		BatchOutput create(BatchOutputContext ctx);
	}
}
