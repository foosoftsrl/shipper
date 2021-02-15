package shipper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import it.foosoft.shipper.core.FileWalker;
import it.foosoft.shipper.core.Pipeline;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.core.PipelineBuilder;
import it.foosoft.shipper.plugins.DefaultPluginFactory;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class Shipper implements Callable<Integer> {

	private static final Logger LOG = LoggerFactory.getLogger(Shipper.class);
	
	static ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

	@Option(names = {"-p", "--pipeline"}, description = "logstash pipeline (single file), overrides --pipelines")
    private File pipelineFile = null;
	
	@Option(names = {"--pipelines"}, description = "logstash pipelines")
    private File pipelinesFile = new File("/etc/shipper/pipelines.yml");

	@Option(names = {"-t", "--thread-count"}, description = "Threads used for filtering stage")
    private int threadCount = Runtime.getRuntime().availableProcessors();

    @Option(names = {"-b", "--batch-size"}, description = "Batch size for filtering and output stage")
    private int batchSize = 512;

    public static void main(String[] args) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException {
        int exitCode = new CommandLine(new Shipper()).execute(args);
        System.exit(exitCode);
    }

    
	public static class PipelineCfg {
		@JsonProperty("pipeline.id")
		String id;
		@JsonProperty("path.config")
		String path;
	}

	@Override
    public Integer call() throws Exception {


    	AtomicBoolean stopRequest = new AtomicBoolean(false);
	    AtomicBoolean stopped = new AtomicBoolean(false);
	    
	    Configuration cfg = new Configuration(threadCount, batchSize);
	    Pipeline pipeline = null;
	    
	    if(pipelineFile != null) {
		    pipeline = PipelineBuilder.build(DefaultPluginFactory.INSTANCE, cfg, pipelineFile);
	    } else {
		    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		    PipelineCfg[] pipelines = mapper.readValue(pipelinesFile, PipelineCfg[].class);
		    if(pipelines.length != 1) {
		    	throw new UnsupportedOperationException("Just support for 1 pipeline, sorry");
		    }
		    byte[] buf;
		    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			    for(PipelineCfg pipelineCfg : pipelines) {
			    	Path globPattern = pipelinesFile.getParentFile().toPath().resolve(pipelineCfg.path);
			        for(Path path: FileWalker.walk(globPattern.toString().replace("\\", "/"))) {
						System.err.println("Found " + path.toString());
						Files.copy(path, baos);
			        };
			    }
			    buf = baos.toByteArray();
		    }
		    try (ByteArrayInputStream bais = new ByteArrayInputStream(buf)) {
		    	pipeline = PipelineBuilder.build(DefaultPluginFactory.INSTANCE, cfg, bais);
		    }
	    }

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
    			LOG.info("Shutdown hook completed");
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
				LOG.info("processed = " + countNow + " evt/s = " + (processed / elapsedSecs) + " queues = " + pipeline.getQueueSizes());
			}		
			LOG.info("Stopping pipeline...");
			pipeline.stop();
			LOG.info("Cleanly exiting...");
		} catch(Exception e) {
			LOG.error("Exiting with exception", e);
		} finally {
			synchronized(stopped) {
				stopped.set(true);
				stopped.notifyAll();
			}
		}
		return 0;
	}
}
