package it.foosoft.shipper.plugins;

import java.util.regex.Pattern;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FilterPlugin;
import it.foosoft.shipper.api.ConfigurationParm;

public class FarmUnicaPathFilter implements FilterPlugin {
	@ConfigurationParm
	public String source;
	
	@ConfigurationParm
	public String prefix = "/farmunica/\\d\\d\\d\\d/\\d\\d";

	private Pattern pattern;

	@Override
	public boolean process(Event e) {
		Object attrValueObj = e.getField(source);
		if(!(attrValueObj instanceof String))
			return false;
		String path = (String)attrValueObj;
		var matcher = pattern.matcher(path);
		if(!matcher.find(0)) {
			return false;
		}
		e.setField("cmsId", matcher.group(1));
		e.setField("jobId", matcher.group(2));
		String rest = matcher.group(3);
		String[] split = rest.split("/");
		String flavourDir = split[0].toLowerCase();
		if(flavourDir.startsWith("dash")) {
			e.setField("uriType", "dash");
			if(split.length == 2 && split[1].endsWith("mpd")) {
				e.setField("dashManifest", split[1]);
			}
		} else if(flavourDir.startsWith("smooth")) {
			e.setField("uriType", "smooth");
		} else if(flavourDir.startsWith("hls")) {
			if(split.length == 2 && split[1].endsWith("m3u8")) {
				e.setField("hlsManifest", split[1]);
			} else if(split.length == 3 && split[2].endsWith("m3u8")) {
				e.setField("hlsLevel", split[1]);
				e.setField("hlsManifest", split[2]);
			} else if(split.length == 3 && split[2].endsWith("ts")) {
				e.setField("hlsLevel", split[1]);
				e.setField("hlsVideoChunk", split[2]);
			} else if(split.length == 3 && split[2].endsWith("aac")) {
				e.setField("hlsLevel", split[1]);
				e.setField("hlsAudioChunk", split[2]);
			}
			e.setField("uriType", "hls");
		} else if(flavourDir.startsWith("mp4")) {
			e.setField("uriType", "mp4");
		} else {
			return false;
		}
		return true;
	}

	@Override
	public void start() {
		pattern = Pattern.compile("^" + prefix + "/(\\d+)_([a-fA-F0-9]+)/(.*)");
	}

	@Override
	public void stop() {
		// Nothing to do...
	}

}
