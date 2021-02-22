package it.foosoft.shipper.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.foosoft.shipper.api.BatchOutput;
import it.foosoft.shipper.api.BatchOutputContext;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.StringProvider;
import it.foosoft.shipper.plugins.elastic.BulkRequestInputStream;

/**
 * Elasticsearch output
 * 
 * 
 * 
 * @author luca
 */
public class ElasticSearchOutput implements BatchOutput {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchOutput.class);

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
	
	private RestClient client;

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

	public static BatchOutput.Factory factory = ElasticSearchOutput::new;

	public ElasticSearchOutput(BatchOutputContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void start() {
		HttpHost[] httpHosts = Arrays.stream(hosts).map(h->{
			String[] split = h.split(":");
			if(split.length != 2) {
				throw new IllegalArgumentException("Invalid host: " + h);
			}
			return new HttpHost(split[0], Integer.parseInt(split[1]));
		}).toArray(HttpHost[]::new);


		Header[] defaultHeaders = new Header[0];
		if(compress)
		  defaultHeaders = new Header[]{new BasicHeader("Content-Encoding", "gzip")};
		
		client = RestClient.builder(httpHosts)
				.setHttpClientConfigCallback(this::configureHttpAsyncClientBuilder)
				.setDefaultHeaders(defaultHeaders)
				.build();
		for(int i = 0; i < outstandingRequests; i++) {
			service.submit(this::poller);
		}
	}
	
	HttpAsyncClientBuilder configureHttpAsyncClientBuilder(HttpAsyncClientBuilder builder) {
		if(user != null && password != null) {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));
				builder.setDefaultCredentialsProvider(credentialsProvider);
		}
		builder.setDefaultIOReactorConfig(IOReactorConfig.custom()
                .setIoThreadCount(outstandingRequests)
                .build());
		return builder;
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
				Request req = new Request("POST", "/_bulk");
				InputStream inStream = new BulkRequestInputStream(index, events, compress);
				req.setEntity(new InputStreamEntity(inStream, ContentType.APPLICATION_JSON));
				long startTime = System.nanoTime();
				Response response = client.performRequest(req);
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() != 200) {
					LOG.info("Server replied {} - {}. Retrying in {}ms", statusLine.getStatusCode(), statusLine.getReasonPhrase());
				} 
				else {
					BulkResponse bulkIndexResponse = mapper.reader().readValue(response.getEntity().getContent(), BulkResponse.class);
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
				LOG.info("Error in communication with the server {}. Retrying...", e.getMessage());
			}
			failureCount++;
			Thread.sleep(retryTime);
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
		try {
			client.close();
		} catch(IOException e) {
			// Swallow and log the IO exception. They won't know what to do with this exception above 
			LOG.warn("Failed stopping ES client: {}", e.getMessage());
		}
	}

}
