package shipper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Output;
import it.foosoft.shipper.core.Pipeline;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.core.PipelineBuilder;
import it.foosoft.shipper.plugins.DefaultPluginFactory;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class Benchmark implements Callable<Integer> {

	static ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

	@Parameters(index = "0", description = "logstash pipeline (single file)")
    private File pipelineFile = null;
	
    @Option(names = {"-t", "--thread-count"}, description = "Threads used for filtering stage")
    private int threadCount = Runtime.getRuntime().availableProcessors();

    @Option(names = {"-b", "--batch-size"}, description = "Batch size for filtering and output stage")
    private int batchSize = 512;

    @Option(names = {"-d", "--dump"}, description = "dump output")
    private boolean dump = false;

	private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException {
        int exitCode = new CommandLine(new Benchmark()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
    	Configuration cfg = new Configuration(threadCount, batchSize);
		Pipeline pipeline = PipelineBuilder.parse(DefaultPluginFactory.INSTANCE, cfg, pipelineFile);
		
		pipeline.addOutput(new Output() {
			@Override
			public void process(Event e) {
				counter.addAndGet(1);
				if(dump) {
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
			int countNow = counter.get();
			int processed = countNow - lastCount;
			double elapsedSecs = (now - lastTime) / 1000000000.0;
			lastCount = countNow;
			lastTime = now;
			System.err.println("processed = " + countNow + " evt/s = " + (processed / elapsedSecs) + " queues = " + pipeline.getQueueSizes());
		}		
		pipeline.stop();
		System.err.println("Happily exiting...");
		return 0;
	}
}
