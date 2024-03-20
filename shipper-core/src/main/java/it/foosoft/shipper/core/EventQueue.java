package it.foosoft.shipper.core;

import it.foosoft.shipper.api.BatchOutputContext;
import it.foosoft.shipper.api.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class EventQueue implements BatchOutputContext {
	private static final List<Event> TERMINATOR = Collections.emptyList();
	private final Timer timer;

	private ArrayList<Event> currentBatch;
	final LinkedBlockingDeque<List<Event>> batchQueue = new LinkedBlockingDeque<>(4);
	private int batchSize;
	private static final Logger LOG = LoggerFactory.getLogger(BatchAdapter.class);
	private boolean eof;

	private AtomicInteger queueSize = new AtomicInteger(0);

	public EventQueue(int batchSize) {
		this.batchSize = batchSize;
		this.currentBatch = new ArrayList<>(batchSize);
		this.timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					flushCurrentBatchIfNotEmpty();
				} catch(InterruptedException e) {
					// Swallow exception
				}
			}
		}, 1000, 100);
	}

	private Object currentBatchMutex = new Object();

	public void process(Event e) {
		synchronized(currentBatchMutex) {
			queueSize.addAndGet(1);
			currentBatch.add(e);
			if(currentBatch.size() == batchSize) {
				try {
					batchQueue.put(currentBatch);
					currentBatch = new ArrayList<>(batchSize);
				} catch (InterruptedException e1) {
					LOG.error("Failed inserting in queue, thread interrupted");
					Thread.currentThread().interrupt();
				}
			}

		}
	}

	// This method is synchronized in order to have a coarser granularity....
	// much better than synchronized all the callers for each event 
	public synchronized List<Event> dequeue() {
		if(eof)
			return TERMINATOR;
		try {
			var list = batchQueue.take();
			if(list == TERMINATOR) {
				LOG.info("Dequeued eof event");
				eof = true;
			}
			queueSize.addAndGet(-list.size());
			return list;
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
			return TERMINATOR;
		}
	}

	public void shutdown() throws InterruptedException {
		timer.cancel();
		flushCurrentBatchIfNotEmpty();
		batchQueue.put(TERMINATOR);
	}

	private void flushCurrentBatchIfNotEmpty() throws InterruptedException {
		synchronized(currentBatchMutex) {
			if (!currentBatch.isEmpty()) {
				batchQueue.put(currentBatch);
				this.currentBatch = new ArrayList<>(batchSize);
			}
		}
	}

	public int queueSize() {
		return queueSize.get();
	}

}
