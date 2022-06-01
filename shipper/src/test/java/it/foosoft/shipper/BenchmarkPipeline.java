package it.foosoft.shipper;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.foosoft.shipper.core.InvalidPipelineException;
import it.foosoft.shipper.core.Pipeline;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.core.PipelineBuilder;
import it.foosoft.shipper.plugins.DebugOutput;
import it.foosoft.shipper.plugins.DefaultPluginFactory;

public class BenchmarkPipeline {

	private static boolean enableDump = false;

	private static final String DISSECT_PIPELINE = "files/dissect.conf";
	private static final String DISSECT_LOGSTASH_PIPELINE = "files/dissect_logstash.conf";
	private static final String CLINK_PIPELINE = "files/clink.conf";
	private static final String CLINK_WITHGROK_PIPELINE = "files/clinkgrok.conf";
	private static final String CLINK_FULL = "files/clinkfull.conf";

	static ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
	public static void main(String[] args) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException, InvalidPipelineException {
		Pipeline pipeline = PipelineBuilder.build(DefaultPluginFactory.INSTANCE, 
				Configuration.MINIMAL, 
				BenchmarkPipeline.class.getResource(CLINK_FULL));
		if(enableDump)
			pipeline.addOutput(new DebugOutput());

		pipeline.start();
		
		long lastCount = 0;
		long lastTime = System.nanoTime();
		for(int i = 0; i < 5; i++) {
			Thread.sleep(1000);
			long now = System.nanoTime();
			long countNow = pipeline.getInputEventCounter();
			long processed = countNow - lastCount;
			double elapsedSecs = (now - lastTime) / 1000000000.0;
			lastCount = countNow;
			lastTime = now;
			System.err.println("processed = " + countNow + " evt/s = " + (processed / elapsedSecs) + " queues = " + pipeline.getQueueSizes());
		}
		
		pipeline.stop();
	}
}
