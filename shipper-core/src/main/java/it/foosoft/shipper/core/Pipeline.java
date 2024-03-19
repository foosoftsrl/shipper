package it.foosoft.shipper.core;

import java.io.File;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.foosoft.shipper.api.Bag;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FilterPlugin;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.Output;
import it.foosoft.shipper.api.PipelineComponent;

public class Pipeline {
	private static final String PIPELINE_STATUS_PATH = "/var/lib/shipper/status";

	private static final Logger LOG = LoggerFactory.getLogger(Pipeline.class);

	public static class Configuration {
		final int numThreads;
		final int batchSize;
		public static final Configuration MINIMAL = new Configuration(1);
		public static final Configuration PERFORMANCE = new Configuration(Runtime.getRuntime().availableProcessors(), 2048);

		public Configuration(int numThreads) {
			this(numThreads, 512);
		}

		public Configuration(int numThreads, int batchSize) {
			this.numThreads = numThreads;
			this.batchSize = batchSize;
		}
		
	}
	
	/**
	 * all the input{} nodes 
	 */  
	private Stage<InputWrapper> inputStage = new Stage<>();
	
	/**
	 * This is all the filter{} nodes flattened
	 */  
	private Stage<Filter> filterStage = new Stage<>();

	/**
	 * all the output{} nodes 
	 */  
	private Stage<Output> outputStage = new Stage<>();

	
	private AtomicLong inputEventsCounter = new AtomicLong(0);

	private AtomicLong outputEventsCounter = new AtomicLong(0);
	
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

	Pipeline(Configuration conf) {
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

	public static interface ParameterParser {
		void accept(InputWrapper wrapper) throws InvalidPipelineException;
	}
	
	public void addInput(Input.Factory inputPlugin, ParameterParser configurator) throws InvalidPipelineException {
		InputWrapper wrapper = new InputWrapper(this, inputPlugin);
		configurator.accept(wrapper);
		inputStage.add(wrapper);
		if(wrapper.getId() != null) {
			Bag props = startupConfig.getBagProperty(wrapper.getId());
			if(props != null) {
				wrapper.setStartStatus(props);
			}
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
			if(!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				LOG.warn("Stopping the filter workers took too long, giving up");
			} 
			else {
				LOG.info("Stopped filter workers");
			}
			// stop the filter stage, this is very easy
			filterStage.stop();
			LOG.info("Stopped filter stage");

			// stop the output stage
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
		for(var input: inputStage) {
			if(input.getId() != null && input.getStopStatus() != null) {
				bag.setBagProperty(input.getId(), input.getStopStatus());
			}
		}
		try {
			File src = new File(PIPELINE_STATUS_PATH);
			new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(src, bag);
		} catch(Exception e) {
			LOG.warn("Can't save filter state: {}", e.getMessage());
		}
	}

	public void processInputEvent(Event e) {
		inputEventsCounter.incrementAndGet();
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
								// We're on main stage, the result *must* be ignored
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
							outputEventsCounter.incrementAndGet();
						}
					}
				}
			}
		} catch(Exception e) {
			LOG.error("Input queue poller exiting on exception ", e);
		}
	}
	
	public Stage<InputWrapper> getInputs() {
		return inputStage;
	}

	public Stage<Filter> getFilteringStage() {
		return filterStage;
	}

	public Stage<Output> getOutputStage() {
		return outputStage;
	}
	
	public void addOutput(Output output) {
		outputStage.add(output);
	}

	public Map<String,Integer> getQueueSizes() {
		Map<String,Integer> queueSizes = new HashMap<>();
		queueSizes.put("processing", queue.queueSize());
		int id = 0;
		for(Output item: outputStage) {
			if(item instanceof BatchAdapter batchAdapter) {
				queueSizes.put("output-" + id, batchAdapter.queueSize());
			}
			id++;
		}
		return queueSizes;
	}

	/**
	 * This method makes no sense... it won't work for conditionals  
	 * 
	 * @param id
	 * @return
	 */
	public FilterPlugin findFilterPluginById(String id) {
		List<FilterWrapper> result = new ArrayList<>();
		filterStage.traverse(f->{
			if(f instanceof FilterWrapper wrapper && wrapper.getId().equals(id))
				result.add(wrapper);
		});
		if(result.isEmpty())
			return null;
		return result.get(0).getInner();
	}

	/**
	 * Find a plugin by class name. Method useful for tests!  
	 * 
	 * @param clazz 
	 * @return
	 */
	public <T extends FilterPlugin> T findFilterPluginByClass(Class<T> clazz) {
		List<T> result = new ArrayList<>();
		filterStage.traverse(f->{
			if(f.getClass().isAssignableFrom(clazz))
				result.add((T)f);
		});
		if(result.isEmpty())
			return null;
		return result.get(0);
	}

	/**
	 * Enumerate all PipelineComponent. Note that filter plugins are not components, they have simpler APIs
	 * which are "decorated" by {@link FilterWrapper}
	 * 
	 * @return
	 */
	public List<PipelineComponent> getComponents() {
		var list = new ArrayList<PipelineComponent>();
		filterStage.traverse(list::add);
		inputStage.traverse(list::add);
		outputStage.traverse(list::add);
		return list;
	}

	public long getInputEventCounter() {
		return inputEventsCounter.get();
	}

	public long getOutputEventCounter() {
		return outputEventsCounter.get();
	}


}