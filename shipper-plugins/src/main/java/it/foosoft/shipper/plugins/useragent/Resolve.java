package it.foosoft.shipper.plugins.useragent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.foosoft.shipper.plugins.UserAgentFilter;
import ua_parser.Client;
import ua_parser.Parser;

public class Resolve {
	public static void main(String[] args) throws JsonProcessingException {
		Parser uaParser = new Parser();
		Client client = uaParser.parse(args[0]);
		System.err.println(client);
		
		System.err.println(new ObjectMapper().writeValueAsString(UserAgentFilter.toMap(client)));
	}
}
