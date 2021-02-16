package it.foosoft.shipper.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.foosoft.shipper.api.Bag;
import it.foosoft.shipper.api.BatchOutput;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.Input.Factory;
import it.foosoft.shipper.api.Output;

public class Pipeline {
	private static final String PIPELINE_STATUS_PATH = "/var/lib/shipper/status";

	private static final Logger LOG = LoggerFactory.getLogger(Pipeline.class);

	public static class Configuration {
		final int numThreads;
		final int batchSize;
		public static Configuration MINIMAL = new Configuration(1);
		public static Configuration PERFORMANCE = new Configuration(Runtime.getRuntime().availableProcessors());

		public Configuration(int numThreads) {
			this(numThreads, 512);
		}

		public Configuration(int numThreads, int batchSize) {
			this.numThreads = numThreads;
			this.batchSize = batchSize;
		}
		
	}
	
	Stage<Input> inputStage = new Stage<>(this);
	
	/**
	 * This is all the filter{} nodes flattened
	 */  
	Stage<Filter> filterStage = new Stage<>(this);

	Stage<Output> outputStage = new Stage<>(this);

	List<InputContextImpl> inputContexts = new ArrayList<>();
	
	AtomicLong inputCounter = new AtomicLong(0);
	AtomicLong outputCounter = new AtomicLong(0);
	
	ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {
		AtomicInteger id = new AtomicInteger(0);
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName("Filter Processor #" + id.addAndGet(1));
			return t;
		}
	});
	
	final EventQueue queue;

	private Configuration configuration;

	private BagImpl startupConfig = new BagImpl();

	Pipeline(Configuration conf) throws JsonParseException, JsonMappingException, IOException {
		this.configuration = conf;
		queue = new EventQueue(conf.batchSize);
		try {
			File src = new File(PIPELINE_STATUS_PATH);
			if(src.exists())
				startupConfig = new ObjectMapper().readValue(src, BagImpl.class);
		} catch(Exception e) {
			LOG.warn("Can't load retrieved state");
		}
	}

	public void addInput(Factory inputPlugin, Consumer<InputWrapper> configurator) {
		InputContextImpl ctx = new InputContextImpl(evt->{
			processInputEvent(evt);
		});
		inputContexts.add(ctx);
		Input input = inputPlugin.create(ctx);
		InputWrapper wrapper = new InputWrapper(input);
		configurator.accept(wrapper);
		inputStage.add(input);
		ctx.setId(wrapper.getId());
		if(wrapper.getId() != null) {
			Bag props = startupConfig.getBagProperty(wrapper.getId());
			if(props != null) {
				ctx.setStartStatus(props);
			}
		}
	}

	public Output addOutput(it.foosoft.shipper.api.PipelineComponent.Factory outputPlugin) {
		if(outputPlugin instanceof Output.Factory) {
			Output plugin = ((Output.Factory)outputPlugin).create();
			outputStage.add(plugin);
			return plugin;
		} else if(outputPlugin instanceof BatchOutput.Factory) {
			BatchAdapter plugin = new BatchAdapter((BatchOutput.Factory)outputPlugin, configuration.batchSize);
			outputStage.add(plugin);
			return plugin;
		} else {
			throw new IllegalArgumentException("Invalid output plugin, must either implement Output or BatchOutput interfaces");
		}
	}

	public void start() {
		outputStage.start();
		filterStage.start();
		inputStage.start();

		for(int i = 0; i < configuration.numThreads; i++) {
			executor.submit(()->{
				pollQueue();
				return null;
			});
		}
	}

	public void stop() throws InterruptedException {
		try {
			// The input know how to stop themselves
			LOG.info("Stopping input stage");
			inputStage.stop();
			LOG.info("Stopped input stage");
	
			// shutdown the queue, this will cause the poller threads to stop
			queue.shutdown();
			LOG.info("Stopped input queue");
			// now wait for the pollers to stop
			executor.shutdown();
			if(!executor.awaitTermination(5, TimeUnit.SECONDS)) {
				LOG.warn("Stopping the filter workers took too long, giving up");
			} 
			else {
				LOG.info("Stopped filter workers");
			}
			// stop the filter stage, this is very easy
			filterStage.stop();
			LOG.info("Stopped filter stage");
			// now 
			outputStage.stop();
			LOG.info("Stopped output filters");
			
			savePipelineStatus();
		} catch(Exception e) {
			LOG.error("Error while stopping pipeline", e);
			throw e;
		}
	}

	private void savePipelineStatus() {
		BagImpl bag = new BagImpl();
		for(var inputCtx: inputContexts) {
			if(inputCtx.getId() != null && inputCtx.getStopStatus() != null) {
				bag.setBagProperty(inputCtx.getId(), inputCtx.getStopStatus());
			}
		}
		try {
			File src = new File(PIPELINE_STATUS_PATH);
			new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(src, bag);
		} catch(Exception e) {
			LOG.warn("Can't save filter state: " + e.getMessage());
		}
	}

	public void processInputEvent(Event e) {
		inputCounter.incrementAndGet();
		queue.process(e);
	}
	
	public void pollQueue() {
		try {
			while(true) {
				List<Event> evtList = queue.dequeue();
				if(evtList.isEmpty()) {
					LOG.info("Exiting poller");
					return;
				}
				for(Event evt: evtList) {
					if(!evt.canceled()) {
						for(var filter: filterStage) {
							try {
								filter.process(evt);
							} catch(Exception e) {
								LOG.error("Canceling event due to processing failure. Probably, a filter misbehaving", e);
								evt.cancel();
							}
							if(evt.canceled())
								break;
						}
						if(!evt.canceled()) {
							for(var output: outputStage) {
								output.process(evt);
							}
							outputCounter.incrementAndGet();
						}
					}
				}
			}
		} catch(Exception e) {
			LOG.error("Input queue poller exiting on exception ", e);
		}
	}
	
	public Stage<Input> getInputs() {
		return inputStage;
	}

	public Stage<Filter> getFilteringStage() {
		return filterStage;
	}

	public Stage<Output> getOutput() {
		return outputStage;
	}
	
	public void addOutput(Output output) {
		outputStage.add(output);
	}

	public Map<String,Integer> getQueueSizes() {
		Map<String,Integer> queueSizes = new HashMap<>();
		queueSizes.put("processing", queue.size());
		int id = 0;
		for(Output queue: outputStage) {
			if(queue instanceof BatchAdapter) {
				queueSizes.put("output-" + id, ((BatchAdapter)queue).queue.size());
			}
			id++;
		}
		return queueSizes;
	}

	public Filter findFilterById(String id) {
		for(var filter: filterStage) {
			if(filter instanceof FilterWrapper) {
				FilterWrapper wrapper = (FilterWrapper)filter;
				if(wrapper.getId().equals(id))
					return wrapper.getInner();
				}
		}
		return null;
	}
	
	public long getInputCounter() {
		return inputCounter.get();
	}

	public long getOutputCounter() {
		return outputCounter.get();
	}


}