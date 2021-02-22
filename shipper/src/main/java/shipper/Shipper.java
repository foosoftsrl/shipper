package shipper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import it.foosoft.shipper.api.FileWalker;
import it.foosoft.shipper.core.Pipeline;
import it.foosoft.shipper.core.Pipeline.Configuration;
import it.foosoft.shipper.core.PipelineBuilder;
import it.foosoft.shipper.plugins.DebugOutput;
import it.foosoft.shipper.plugins.DefaultPluginFactory;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class Shipper implements Callable<Integer> {

	private static final Logger LOG = LoggerFactory.getLogger(Shipper.class);
	
	static ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

	@Option(names = {"--pipeline"}, description = "logstash pipeline (single file), overrides --pipelines")
    private File pipelineFile = null;
	
	@Option(names = {"-p", "--pipelines"}, description = "logstash pipelines")
    private File pipelinesFile = new File("/etc/shipper/pipelines.yml");

	@Option(names = {"-t", "--thread-count"}, description = "Threads used for filtering stage")
    private int threadCount = Runtime.getRuntime().availableProcessors();

    @Option(names = {"-b", "--batch-size"}, description = "Batch size for filtering and output stage")
    private int batchSize = 512;

    @Option(names = {"-o"}, description = "Remove all outputs and replace them with a file output. Use '-' for stdout")
    private String output = null;

    @Option(names = {"-i"}, description = "Remove all input and replace them with a (single) file input")
    private String input = null;

    public static void main(String[] args) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException {
        int exitCode = new CommandLine(new Shipper()).execute(args);
        System.exit(exitCode);
    }

    
	public static class PipelineCfg {
		@JsonProperty("pipeline.id")
		String id;

		@JsonProperty("pipeline.workers")
		Integer workers;
		
		@JsonProperty("pipeline.batch.size")
		Integer batchSize;

		@JsonProperty("path.config")
		String path;
	}

	@Override
    public Integer call() throws Exception {


    	AtomicBoolean stopRequest = new AtomicBoolean(false);
	    AtomicBoolean stopped = new AtomicBoolean(false);
	    
	    Pipeline pipeline = null;
	    
	    if(pipelineFile != null) {
		    Configuration cfg = new Configuration(threadCount, batchSize);
		    pipeline = PipelineBuilder.build(DefaultPluginFactory.INSTANCE, cfg, pipelineFile);
	    } else {
		    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		    PipelineCfg[] pipelines = mapper.readValue(pipelinesFile, PipelineCfg[].class);
		    if(pipelines.length != 1) {
		    	throw new UnsupportedOperationException("Just support for 1 pipeline, sorry");
		    }
		    PipelineCfg pipelineCfg = pipelines[0];

		    byte[] buf;
		    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
		    	Path globPattern = pipelinesFile.getParentFile().toPath().resolve(pipelineCfg.path);
		    	String globPatternStr = globPattern.toString().replace("\\", "/");
		    	LOG.info("Looking for configuration file with glob pattern {}", globPatternStr);
		        List<Path> cfgFilePathList = FileWalker.walk(globPatternStr);
		        cfgFilePathList.sort((a,b)->{
		        	return a.compareTo(b);
		        });
				for(Path path: cfgFilePathList) {
					LOG.info("Found configuration file {}", path.toString());
					Files.copy(path, baos);
		        };
			    buf = baos.toByteArray();
		    }
		    Configuration cfg = new Configuration(
		    		pipelineCfg.workers != null ? pipelineCfg.workers: this.threadCount, 
    				pipelineCfg.batchSize != null ? pipelineCfg.batchSize: this.batchSize
		    );
		    try (ByteArrayInputStream bais = new ByteArrayInputStream(buf)) {
		    	pipeline = PipelineBuilder.build(DefaultPluginFactory.INSTANCE, cfg, bais);
		    }
	    }
	    if(output != null) {
	    	pipeline.getOutputStage().clear();
	    	File outputFile = null;
	    	if(!output.equals("-")) {
	    		outputFile = new File(output);
	    	}
	    	pipeline.getOutputStage().add(new DebugOutput(outputFile));
	    }

	    if(input != null) {
	    	pipeline.getInputs().clear();
	    	pipeline.addInput(ctx->{
	    		return new ShipperAdHocInput(ctx, input, ()->{
            		stopRequest.set(true);
	    		});
	    	}, unused->{});
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
			long lastCount = 0;
			long lastTime = System.nanoTime();
			while(!stopRequest.get()) {
				synchronized(stopRequest) {
					stopRequest.wait(1000);
				}
				long now = System.nanoTime();
				long countNow = pipeline.getOutputCounter();
				long processed = countNow - lastCount;
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
