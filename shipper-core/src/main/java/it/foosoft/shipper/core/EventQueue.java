package it.foosoft.shipper.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.BatchOutput;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Output;

public class EventQueue implements Output {
	LinkedBlockingDeque<Event> queue = new LinkedBlockingDeque<>();
	private int batchSize;
	private static final Logger LOG = LoggerFactory.getLogger(BatchAdapter.class);
	public BatchOutput batchOutput;
	private boolean eof;

	public EventQueue(int batchSize) {
		this.batchSize = batchSize;
		this.queue = new LinkedBlockingDeque<>(batchSize * 32);
	}

	@Override
	public void start() {
		batchOutput.start();
	}

	@Override
	public void stop() {
		batchOutput.stop();
	}

	@Override
	public void process(Event e) {
		try {
			queue.put(e);
		} catch (InterruptedException e1) {
			LOG.error("Failed inserting in queue, thread interrupted");
			Thread.currentThread().interrupt();
		}
	}

	// This method is synchronized in order to have a coarser granularity....
	// much better than synchronized all the callers for each event 
	public synchronized List<Event> dequeue() {
		List<Event> list = new ArrayList<>();
		if(eof)
			return list;
		while(list.size() < batchSize) {
			try {
				Event evt = queue.take();
				if(evt == EventImpl.VOID) {
					LOG.info("Dequeued eof event");
					eof = true;
					break;
				}
				list.add(evt);
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		return list;
	}

	public void shutdown() throws InterruptedException {
		queue.put(EventImpl.VOID);
	}

	public int size() {
		return queue.size();
	}

}
