package it.foosoft.shipper.plugins;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.plugins.geoip.GeoIPFilter;

public class GeoIpFilter implements Filter {

	@NotNull
	@ConfigurationParm
	public String source;

	@NotNull
	@ConfigurationParm
	public String target = "geoip";

	@ConfigurationParm
	public String[] fields = null;

	@ConfigurationParm
	public String database;

	@ConfigurationParm
	public String default_database_type;

	@ConfigurationParm
	public Integer cache_size = 1000;
	
	@ConfigurationParm
	public String[] tag_on_failure = new String[] {"_geoip_lookup_failure"};
	

	private GeoIPFilter geoIpFilter; 
	
	@Override
	public boolean process(Event e) {
		String sourceField = e.getFieldAsString(source);
		if(sourceField == null) {
			return false;
		}
		if(!geoIpFilter.handleEvent(e)) {
			e.addTags(tag_on_failure);
			return false;
		}
		return true;
	}

	@Override
	public void start() {
		String db = database;
		if(db == null) {
			throw new UnsupportedOperationException("No support for logstash's default databases"); 
		}
		if(source == null) {
			throw new IllegalStateException("GeoIp filter's Source Configuration Option is mandatory");
		}

		geoIpFilter = new GeoIPFilter(source, target, fields == null ? null: Arrays.asList(fields), database, cache_size);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}
}
