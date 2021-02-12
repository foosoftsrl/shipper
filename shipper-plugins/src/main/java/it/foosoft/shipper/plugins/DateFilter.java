package it.foosoft.shipper.plugins;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Param;

public class DateFilter implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(DateFilter.class);
	@Param
	public String locale;

	@Param
	public String timezone;

	@Param
	public String[] match;

	@Param
	public String[] remove_field;

	private String fieldName;

	ThreadLocal<SimpleDateFormat> dateParser;	
	@Override
	public void process(Event evt) {
		Object obj = evt.getField(fieldName);
		if(!(obj instanceof String)) {
			fail();
			return;
		}
		String dateAsText = (String)obj;
		try {
			Date d = dateParser.get().parse(dateAsText);
			evt.setTimestamp(d);
		} catch (Exception e) {
			LOG.info("Failed parsing date {}", dateAsText);
			fail();
			return;
		}
		evt.removeFields(remove_field);
		return;
	}

	private void fail() {
	}

	@Override
	public void start() {
		if(match.length != 2) {
			throw new IllegalArgumentException("DateFilter.match currently supports only 2 arguments");
		}
		fieldName = match[0];
		dateParser = ThreadLocal.withInitial(() -> new SimpleDateFormat(match[1]));		
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
