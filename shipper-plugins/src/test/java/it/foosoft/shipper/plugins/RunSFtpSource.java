package it.foosoft.shipper.plugins;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.foosoft.shipper.core.InputContextImpl;

public class RunSFtpSource {
	public static void main(String[] args) {
		ObjectWriter mapper = new ObjectMapper().writerWithDefaultPrettyPrinter();
		InputContextImpl inputContext = new InputContextImpl(evt->{
			try {
				System.err.println("Here's a line: " + mapper.writeValueAsString(evt));
			} catch (JsonProcessingException e1) {
				System.err.println("Here's a line... but can't JSON-print int");
			}
		});
		SftpInput input = new SftpInput(inputContext);
		input.username = "mediaset-logs";
		input.password = "QMZvTtqXMFJUMyR";
		input.remoteHost = "mediaset-logs.ingest.cdn.level3.net";
		input.olderThan = 30;
		input.remoteDirectory = "/logs/caching/2021/02/08/vod01.msf.cdn.mediaset.net";
		input.start();
	}
}
