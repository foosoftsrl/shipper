package it.foosoft.shipper.plugins;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Param;
import it.foosoft.shipper.plugins.geoip.GeoIPFilter;

public class GeoIpFilter implements Filter {
	@Param
	public String source;

	@Param
	public String target;

	@Param
	public String database;

	@Param
	public String default_database_type;

	@Param
	public Integer cache_size = 1000;
	
	@Param
	public String[] tag_on_failure = new String[]{"_geoip_lookup_failure"};
	

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
		geoIpFilter = new GeoIPFilter(source, target, null, database, cache_size);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}
}
