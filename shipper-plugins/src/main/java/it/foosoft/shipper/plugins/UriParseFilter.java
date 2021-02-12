package it.foosoft.shipper.plugins;

import java.net.URI;
import java.net.URISyntaxException;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Param;

public class UriParseFilter implements Filter {
	@Param
	public String source;
	
	@Param
	public String tag_on_failure;
	
	@Param
	public String[] remove_field;

	@Override
	public void process(Event e) {
		Object fieldValue = e.getField(source);
		if(!(fieldValue instanceof String)) {
			fail();
		} else {
			try {
				URI uri = new URI((String)fieldValue);
				if(uri.getHost() != null)
					e.setField("host", uri.getHost());
				if(uri.getPath() != null)
					e.setField("path", uri.getPath());
				if(uri.getQuery() != null)
					e.setField("queryString", uri.getQuery());
				if(uri.getFragment() != null)
					e.setField("path", uri.getFragment());
			} catch (URISyntaxException e1) {
				fail();
			}
		}
		return;
	}

    private void fail() {
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
