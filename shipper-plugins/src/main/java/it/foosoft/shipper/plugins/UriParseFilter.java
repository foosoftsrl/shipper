package it.foosoft.shipper.plugins;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Param;

public class UriParseFilter implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(UriParseFilter.class);
	
	@Param
	public String source;
	
	@Param
	public String[] tag_on_failure = new String[0];
	
	@Override
	public boolean process(Event evt) {
		Object fieldValue = evt.getField(source);
		if(!(fieldValue instanceof String)) {
			LOG.warn("Input is not a string...");
			handleFailure(evt);
			return false;
		} else {
			try {
				URI uri = new URI((String)fieldValue);
				if(uri.getHost() != null)
					evt.setField("host", uri.getHost());
				if(uri.getPath() != null)
					evt.setField("path", uri.getPath());
				if(uri.getQuery() != null)
					evt.setField("queryString", uri.getQuery());
				if(uri.getFragment() != null)
					evt.setField("path", uri.getFragment());
			} catch (URISyntaxException e1) {
				handleFailure(evt);
				LOG.warn("Failed parsing url {}", fieldValue);
				return false;
			}
		}
		return true;
	}
	
	public void handleFailure(Event e) {
		e.addTags(tag_on_failure);
	}

	@Override
	public void start() {
		// Nothing to do...
	}

	@Override
	public void stop() {
		// Nothing to do...
	}

}
