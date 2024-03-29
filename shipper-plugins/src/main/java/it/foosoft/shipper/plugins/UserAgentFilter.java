package it.foosoft.shipper.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FilterPlugin;
import ua_parser.Client;
import ua_parser.Parser;

public class UserAgentFilter implements FilterPlugin {

	private static final Logger LOG = LoggerFactory.getLogger(UserAgentFilter.class);
	@ConfigurationParm(description="name of the field to parse")
	public String source;
	
	@ConfigurationParm(description="path to ua-parser regexes.yaml (see github)")
	public File regexes;

	@ConfigurationParm
	public String target;

	@ConfigurationParm
	public int cacheSize = 32768;

	private Parser uaParser;

	Map<String, Client> lruMap = Collections.synchronizedMap(new LRUMap<>(65536));

	Cache<String, Client> cache = Caffeine.newBuilder()
			  .expireAfterWrite(15, TimeUnit.MINUTES)
			  .maximumSize(cacheSize)
			  .build();
	
	@Override
	public void start() {
		if(regexes == null)
			uaParser = new Parser();
		else {
			try (FileInputStream istr = new FileInputStream(regexes)) {
				uaParser = new Parser(istr);
			} catch (IOException e) {
				throw new IllegalStateException("Invalid regexes file " + regexes.getAbsolutePath());
			}
		}
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

		Client client = cache.get(uaString, (String uaString_)->{
			return uaParser.parse(uaString_);
		});
		
		if(client != null) {
			e.setField("userAgent", toMap(client));
		}	
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
