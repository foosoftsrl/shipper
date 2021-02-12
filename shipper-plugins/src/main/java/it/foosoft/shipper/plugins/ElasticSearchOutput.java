package it.foosoft.shipper.plugins;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;

import it.foosoft.shipper.api.BatchOutput;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.OutputContext;
import it.foosoft.shipper.api.Param;
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

	@Param
	public String[] hosts = new String[] {"127.0.0.1:9200"};
	
	@Param
	public String index;
	
	@Param
	public String user;

	@Param
	public String password;

	@Param
	private int outstandingRequests = 8;
	
	@Param
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

	private OutputContext ctx;

	public static BatchOutput.Factory factory = ElasticSearchOutput::new;

	public ElasticSearchOutput(OutputContext ctx) {
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
		return builder;
	}

	ObjectMapper mapper = new ObjectMapper();

	static class BulkResponse {
		public long took;
		public boolean errors;
		public Object[] items;
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
				sendAndRetry(events);
			}
		} catch(Exception e) {
			LOG.error("Exiting from poller due to exception", e);
			return null;
		}
		LOG.info("Exiting!");
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
						LOG.info("Response has errors... not retrying... data will be lost. Sorry about that");
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
		service.shutdownNow();
		try {
			client.close();
		} catch(IOException e) {
			// Swallow and log the IO exception. They won't know what to do with this exception above 
			LOG.warn("Failed stopping ES client: {}", e.getMessage());
		}
	}

}
