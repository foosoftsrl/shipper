package it.foosoft.shipper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Output;
import it.foosoft.shipper.core.Pipeline;
import it.foosoft.shipper.core.PipelineBuilder;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.plugins.DefaultPluginFactory;

public class BenchmarkPipeline {

	private static boolean enableDump = false;
	private static int count = 0;

	private static final String DISSECT_PIPELINE = "files/dissect.conf";
	private static final String DISSECT_LOGSTASH_PIPELINE = "files/dissect_logstash.conf";
	private static final String CLINK_PIPELINE = "files/clink.conf";
	private static final String CLINK_WITHGROK_PIPELINE = "files/clinkgrok.conf";
	private static final String CLINK_FULL = "files/clinkfull.conf";

	static ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
	public static void main(String[] args) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException {
		Pipeline pipeline = PipelineBuilder.parse(DefaultPluginFactory.INSTANCE, Configuration.MINIMAL, BenchmarkPipeline.class.getResource(CLINK_FULL));
		pipeline.addOutput(new Output() {

			@Override
			public void process(Event e) {
				count++;
				if(enableDump) {
					try {
						System.err.println(writer.writeValueAsString(e));
					} catch (JsonProcessingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void start() {
			}

			@Override
			public void stop() {
			}
		});

		pipeline.start();
		
		int lastCount = 0;
		long lastTime = System.nanoTime();
		for(int i = 0; i < 600; i++) {
			Thread.sleep(1000);
			long now = System.nanoTime();
			int countNow = count;
			int processed = count - lastCount;
			double elapsedSecs = (now - lastTime) / 1000000000.0;
			lastCount = countNow;
			lastTime = now;
			System.err.println("processed = " + count + " evt/s = " + (processed / elapsedSecs) + " queues = " + pipeline.getQueueSizes());
		}
		
		pipeline.stop();
	}
}
