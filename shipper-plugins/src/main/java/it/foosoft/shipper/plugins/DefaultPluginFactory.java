package it.foosoft.shipper.plugins;

import it.foosoft.shipper.api.FilterPlugin;
import it.foosoft.shipper.api.Input;
import it.foosoft.shipper.api.PipelineComponent;
import it.foosoft.shipper.api.PluginManager;

public class DefaultPluginFactory implements PluginManager {

	public static DefaultPluginFactory INSTANCE = new DefaultPluginFactory();
	
	private DefaultPluginFactory() {
		
	}

	public Input.Factory findInputPlugin(String name) {
		if (name.equals("sftp")) {
			return SftpInput::new;
		}
		else if (name.equals("testfile")) {
			return TestInput::new;
		}
		else if (name.equals("file")) {
			return FileInput::new;
		}
		throw new RuntimeException("No such input plugin: " + name);
	}
	
	public FilterPlugin.Factory findFilterPlugin(String name) {
		if (name.equals("mutate")) {
			return MutateFilter::new;
		} else if (name.equals("dissect")) {
			return LogstashDissectFilter::new;
		} else if (name.equals("dissect_regex")) {
			return DissectFilter::new;
		} else if (name.equals("dissect_logstash")) {
			return LogstashDissectFilter::new;
		} else if (name.equals("grok")) {
			return GrokFilter::new;
		} else if (name.equals("date")) {
			return DateFilter::new;
		} else if (name.equals("uriparse")) {
			return UriParseFilter::new;
		} else if (name.equals("urldecode")) {
			return UrlDecodeFilter::new;
		} else if (name.equals("geoip")) {
			return GeoIpFilter::new;
		} else if (name.equals("drop")) {
			return DropFilter::new;
		} else if (name.equals("farmunicapath")) {
			return FarmUnicaPathFilter::new;
		} else if (name.equals("useragent")) {
			return UserAgentFilter::new;
		}
		throw new RuntimeException("No such filter plugin: " + name);
	}

	@Override
	public PipelineComponent.Factory findOutputPlugin(String name) {
		if(name.equals("elasticsearch"))
			return ElasticSearchOutput.factory;
		if(name.equals("elasticsearch_sync"))
			return ElasticSearchOutputSync.factory;
		throw new RuntimeException("No such output plugin: " + name);
	}
}
