package it.foosoft.shipper.plugins;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Param;
import ua_parser.Client;
import ua_parser.Parser;

public class UserAgentFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(UserAgentFilter.class);
	@Param(description="name of the field to parse")
	public String source;
	
	@Param(description="path to ua-parser regexes.yaml (see github)")
	public String regexes;

	@Param
	public String target;

	private Parser uaParser;

	Map<String, Client> lruMap = Collections.synchronizedMap(new LRUMap<>(65536));

	@Override
	public void start() {
		uaParser = new Parser();
	}

	@Override
	public void stop() {
	}

	@Override
	public boolean process(Event e) {
		String uaString = e.getFieldAsString(source);
		if(uaString == null) {
			return false;
		}
		Client client = lruMap.computeIfAbsent(uaString, uaString_->{
			//LOG.info("Computing damned user agent, size = " + lruMap.size());
			return uaParser.parse(uaString_);
		});
		
		e.setField("userAgent", toMap(client));
		return true;
	}

	public static Map<String, String> toMap(Client client) {
		Map<String,String> userAgent = new HashMap<>();
		userAgent.put("name", client.userAgent.family);
		userAgent.put("device", client.device.family);
		userAgent.put("os_name", client.os.family);
		userAgent.put("os_major", client.os.major);
		userAgent.put("os_minor", client.os.minor);
		return userAgent;
	}

}
