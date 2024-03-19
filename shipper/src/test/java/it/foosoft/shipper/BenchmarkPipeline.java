package it.foosoft.shipper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.foosoft.shipper.api.BatchOutput;
import it.foosoft.shipper.core.*;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.plugins.DebugOutput;
import it.foosoft.shipper.plugins.DefaultPluginFactory;

public class BenchmarkPipeline {

	private static boolean enableDump = false;

	private static final String DISSECT_PIPELINE = "files/dissect.conf";
	private static final String DISSECT_LOGSTASH_PIPELINE = "files/dissect_logstash.conf";
	private static final String CLINK_PIPELINE = "files/clink.conf";
	private static final String CLINK_WITHGROK_PIPELINE = "files/clinkgrok.conf";
	private static final String CLINK_FULL = "files/clinkfull.conf";

	public static void main(String[] args) throws Exception {
		Pipeline pipeline = PipelineBuilder.build(DefaultPluginFactory.INSTANCE, 
				Configuration.PERFORMANCE,
				BenchmarkPipeline.class.getResource(CLINK_PIPELINE));
		if(enableDump)
			pipeline.addOutput(new DebugOutput());
		pipeline.addOutput(new BatchAdapter(ctx -> new BatchOutput() {
			List<Thread> threads = new ArrayList<>();
			@Override
			public void start() {
				for(int i = 0; i < 50; i++) {
					Thread t = new Thread(this::run);
					t.start();
					threads.add(t);
				}
			}
			@Override
			public void stop() {
				for(Thread t: threads) {
					t.interrupt();
					do {
						try {
							t.join();
						} catch (Exception e) {

						}
					} while(true);
				}
				threads.clear();
			}
			void run() {
				while(!Thread.interrupted()) {
					ctx.dequeue();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
						return;
                    }
                }
			}
        }));

		pipeline.start();
		
		long lastCount = 0;
		long lastTime = System.nanoTime();
		for(int i = 0; i < 50; i++) {
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
