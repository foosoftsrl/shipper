package shipper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.foosoft.shipper.core.Pipeline;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.core.PipelineBuilder;
import it.foosoft.shipper.plugins.DefaultPluginFactory;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class Shipper implements Callable<Integer> {

	private static final Logger LOG = LoggerFactory.getLogger(Shipper.class);
	
	static ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

	@Option(names = {"-p", "--pipeline"}, description = "logstash pipeline (single file)")
    private File pipelineFile = new File("/etc/shipper/pipeline");
	
    @Option(names = {"-t", "--thread-count"}, description = "Threads used for filtering stage")
    private int threadCount = Runtime.getRuntime().availableProcessors();

    @Option(names = {"-b", "--batch-size"}, description = "Batch size for filtering and output stage")
    private int batchSize = 512;

    public static void main(String[] args) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException {
        int exitCode = new CommandLine(new Shipper()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
	    AtomicBoolean stopRequest = new AtomicBoolean(false);
	    AtomicBoolean stopped = new AtomicBoolean(false);

	    Configuration cfg = new Configuration(threadCount, batchSize);
		Pipeline pipeline = PipelineBuilder.parse(DefaultPluginFactory.INSTANCE, cfg, pipelineFile);
		pipeline.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
            	synchronized(stopRequest) {
            		stopRequest.set(true);
            		stopRequest.notifyAll();
            	}
            	synchronized(stopped) {
            		while(!stopped.get()) {
            			LOG.info("Waiting for main thread...");
        				try {
							stopped.wait(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
            		}
            	}
            }
        });

		
		try {
			int lastCount = 0;
			long lastTime = System.nanoTime();
			while(!stopRequest.get()) {
				synchronized(stopRequest) {
					stopRequest.wait(1000);
				}
				long now = System.nanoTime();
				int countNow = pipeline.getOutputCounter();
				int processed = countNow - lastCount;
				double elapsedSecs = (now - lastTime) / 1000000000.0;
				lastCount = countNow;
				lastTime = now;
				System.err.println("processed = " + countNow + " evt/s = " + (processed / elapsedSecs) + " queues = " + pipeline.getQueueSizes());
			}		
			LOG.info("Stopping pipeline...");
			pipeline.stop();
			LOG.info("Cleanly exiting...");
		} finally {
			synchronized(stopped) {
				stopped.set(true);
				stopped.notifyAll();
			}
		}
		return 0;
	}
}
