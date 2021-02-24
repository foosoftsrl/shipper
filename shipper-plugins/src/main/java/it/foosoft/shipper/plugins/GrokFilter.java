package it.foosoft.shipper.plugins;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;
import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FilterPlugin;

public class GrokFilter implements FilterPlugin {
	private static final Logger LOG = LoggerFactory.getLogger(GrokFilter.class);
	
	@ConfigurationParm
	File[] patterns_dir = new File[0];
	
	@ConfigurationParm
	String[] tag_on_failure = new String[]{"_grokparsefailure"};
	
	@ConfigurationParm
	Map<String,String> match = new HashMap<>();

	@ConfigurationParm
	boolean break_on_match;

	Map<String, Grok> groks;

	@Override
	public boolean process(Event event) {
		boolean atLeastOneMatch = false;
		for(Map.Entry<String,Grok> entry: groks.entrySet()) {
			var fieldName = entry.getKey();
			String fieldValue = event.getFieldAsString(fieldName);
			if(fieldValue == null) {
				continue;
			}
			var grok = entry.getValue();
			Match cm = grok.match(fieldValue);
			if(!Boolean.TRUE.equals(cm.isNull())) {
				Map<String, Object> captured = cm.capture();
				for(var capturedField : captured.entrySet()) {
					if(capturedField.getValue() != null) {
						if(capturedField.getKey().endsWith("_grokfailure")) {
							LOG.warn("Failed conversion of field " + capturedField.getKey().replace("_grokfailure", ""));
						}
						else {
							event.setField(capturedField.getKey(), capturedField.getValue());
						}
					}
				}
				atLeastOneMatch = true;
			}
		}
		if(!atLeastOneMatch) {
			event.addTags(tag_on_failure);
		}
		return atLeastOneMatch;
	}

	@Override
	public void start() {
		GrokCompiler compiler = GrokCompiler.newInstance();
		compiler.registerDefaultPatterns();
		groks = new HashMap<>();
		for(File pattern_dir: patterns_dir) {
			pattern_dir.listFiles((dir,fileName)->{
				File patternFile = new File(dir, fileName);
				LOG.info("Parsing grok file {}", patternFile);
				try(FileReader reader = new FileReader(patternFile)) {
					compiler.register(reader);
				} catch (IOException e) {
					LOG.warn("Failed reading pattern {}: {}", fileName, e.getMessage());
				}
				return false;
			});
		}
		for(Map.Entry<String,String> entry: match.entrySet()) {
			groks.put(entry.getKey(), compiler.compile(entry.getValue()));
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
