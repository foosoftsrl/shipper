package it.foosoft.shipper.plugins;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Param;

public class GrokFilter implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(GrokFilter.class);
	
	@Param
	String patterns_dir = "/etc/logstash/patterns";
	
	@Param
	String tag_on_failure = "_grokparsefailure";
	
	@Param
	Map<String,String> match = new HashMap<>();

	@Param
	String id;

	@Param
	boolean break_on_match;

	Map<String, Grok> groks;
	@Override
	public void process(Event event) {
		for(Map.Entry<String,Grok> entry: groks.entrySet()) {
			var fieldName = entry.getKey();
			String fieldValue = event.getFieldAsString(fieldName);
			if(fieldValue == null) {
				fail();
				return;
			}
			var grok = entry.getValue();
			Match cm = grok.match(fieldValue);
			Map<String, Object> captured = cm.capture();
			for(var capturedField : captured.entrySet()) {
				if(capturedField.getValue() != null)
					event.setField(capturedField.getKey(), capturedField.getValue());
			}
			if(false) {
				try {
					System.err.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(captured));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void fail() {
		
	}
	@Override
	public void start() {
		GrokCompiler compiler = GrokCompiler.newInstance();
		compiler.registerDefaultPatterns();
		groks = new HashMap<>();
		new File(patterns_dir).listFiles((dir,fileName)->{
			try(FileReader reader = new FileReader(new File(dir, fileName))) {
				compiler.register(reader);
			} catch (IOException e) {
				LOG.warn("Failed reading pattern {}: {}", fileName, e.getMessage());
			}
			return false;
		});
		for(Map.Entry<String,String> entry: match.entrySet()) {
			groks.put(entry.getKey(), compiler.compile(entry.getValue()));
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
