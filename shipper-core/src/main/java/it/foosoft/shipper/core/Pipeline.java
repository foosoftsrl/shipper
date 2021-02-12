package it.foosoft.shipper.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.InputContext;
import it.foosoft.shipper.api.Output;

public class Pipeline {
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

	List<InputContext> inputContexts;
	
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

	Pipeline(Configuration conf) {
		this.configuration = conf;
		queue = new EventQueue(conf.batchSize);
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

	public void stop() {
		executor.shutdownNow();
		inputStage.stop();
		filterStage.stop();
		outputStage.stop();
	}

	public void processInputEvent(Event e) {
		queue.process(e);
	}
	
	public void pollQueue() {
		try {
			while(true) {
				List<Event> evtList = queue.dequeue();
				for(Event evt: evtList) {
					if(!evt.canceled()) {
						for(var filter: filterStage) {
							filter.process(evt);
							if(evt.canceled())
								break;
						}
						for(var output: outputStage) {
							output.process(evt);
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

}