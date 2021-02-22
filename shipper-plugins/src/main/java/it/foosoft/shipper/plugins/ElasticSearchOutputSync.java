package it.foosoft.shipper.plugins;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;

import it.foosoft.shipper.api.BatchOutput;
import it.foosoft.shipper.api.BatchOutputContext;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.StringProvider;
import it.foosoft.shipper.plugins.elastic.BulkIndexHeader;

/**
 * Elasticsearch output
 * 
 * 
 * 
 * @author luca
 */
public class ElasticSearchOutputSync implements BatchOutput {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchOutputSync.class);

	@ConfigurationParm
	public String[] hosts = new String[] {"127.0.0.1:9200"};
	
	@ConfigurationParm
	public StringProvider index;
	
	@ConfigurationParm
	public String user;

	@ConfigurationParm
	public String password;

	@ConfigurationParm
	private int outstandingRequests = 8;
	
	@ConfigurationParm
	public boolean compress = true;
	
	private ObjectMapper objectMapper = new ObjectMapper();

	private ExecutorService service = Executors.newCachedThreadPool(new ThreadFactory() {
		AtomicInteger counter = new AtomicInteger(0);
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName("Elastic output #" + counter.addAndGet(1));
			return t;
		}
	});

	private BatchOutputContext ctx;

	public AtomicLong nextServer = new AtomicLong(0);
	
	public static BatchOutput.Factory factory = ElasticSearchOutputSync::new;

	public ElasticSearchOutputSync(BatchOutputContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void start() {
		for(int i = 0; i < outstandingRequests; i++) {
			service.submit(this::poller);
		}
	}
	
	ObjectMapper mapper = new ObjectMapper();

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Shards {
		public int total;
		public int successful;
		public int failed;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Error {
		public String type;
		public String reason;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Index {
		public String _index;
		public String _type;
		public String _id;
		public String _version;
		public String result;
		public String status;
		public String _seq_no;
		public String _primary_term;
		public Error error;
		public Shards _shards;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Item {
		public Index index;
	}

	static class BulkResponse {
		public long took;
		public boolean errors;
		public Item[] items;
	}
	/**
	 * Continously poll the  
	 * @return
	 * @throws InterruptedException
	 */
	private Void poller() throws InterruptedException {
		try {
			while(!Thread.interrupted()) {
				List<Event> events = ctx.dequeue();
				if(events.isEmpty()) {
					break;
				}
				sendAndRetry(events);
			}
		} catch(Exception e) {
			LOG.error("Exiting from poller due to exception", e);
			return null;
		}
		LOG.info("Exiting cleanly");
		return null;
	}
	
	private void sendAndRetry(List<Event> events) throws InterruptedException {
		int failureCount = 0;
		while(true) {
			int retryTime = 100 * (1 << Math.max(failureCount, 8));
			try {
				int idx = (((int)(nextServer.getAndAdd(1) & 0x7ffffffl)) % hosts.length);
				URL url = new URL("http://" + hosts[idx] + "/_bulk");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				long startTime = System.nanoTime();

				con.setRequestMethod("POST");
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setRequestProperty("Accept", "application/json");
				con.setRequestProperty("Content-Type","application/x-ndjson");
				if(user != null && password != null) {
					String auth = user + ":" + password;
					byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
					String authHeaderValue = "Basic " + new String(encodedAuth);
					con.setRequestProperty("Authorization", authHeaderValue);
				}
				con.setChunkedStreamingMode(4096);
				if(compress)
					con.setRequestProperty("Content-Encoding", "gzip");
				try(OutputStream outputStream = con.getOutputStream()) {
					if(compress) {
						try(GZIPOutputStream zipOutputStream = new GZIPOutputStream(outputStream)) {
							writeEvents(zipOutputStream, events);
						}
					} else {
						writeEvents(outputStream, events);
					}
				}
				if(con.getResponseCode() != 200) {
					LOG.info("Server replied {} - {}. Retrying in {}ms", con.getResponseCode(), con.getResponseMessage());
				} else {
					BulkResponse bulkIndexResponse = mapper.reader().readValue(con.getInputStream(), BulkResponse.class);
					if(bulkIndexResponse.errors) {
						for(var a : bulkIndexResponse.items) {
							if(a.index != null) {
								var index = a.index;
								if(index.error != null) {
									LOG.info("Error {} while inserting document: {}", index.error.type, index.error.reason); 
								}
							}
						}
					}
					long elapsed = (System.nanoTime() - startTime) / 1000000;
					LOG.debug("Sent bulk, time = {} es time {}", elapsed, bulkIndexResponse.took);
					break;
				}
			}
			catch(Throwable e) {
				LOG.info("Error in communication with the server {}. Retrying...", e.getMessage(), e);
			}
			failureCount++;
			Thread.sleep(retryTime);
		}	
		
	}

	private void writeEvents(OutputStream outputStream, List<Event> events) throws IOException {
		SequenceWriter sequenceWriter = objectMapper.writer().writeValues(outputStream);
		for(Event evt: events) {
			BulkIndexHeader bulkIndexHeader = new BulkIndexHeader(index.evaluate(evt));
			sequenceWriter.write(bulkIndexHeader);
			outputStream.write('\n');
			sequenceWriter.write(evt);
			outputStream.write('\n');
		}
	}

	@Override
	public void stop() {
		service.shutdown();
		try {
			if(!service.awaitTermination(5, TimeUnit.SECONDS)) {
				LOG.warn("Stopping the worker tasks took too long, giving up");
			}
		} catch (InterruptedException e1) {
			LOG.warn("Interrupted while waiting for service stop");
		}
	}

}
