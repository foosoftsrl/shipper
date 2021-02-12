package it.foosoft.shipper.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.foosoft.shipper.api.Event;

/**
 * A class which interpolates strings such as "...%{field1}...%{field2}..."
 * 
 * @author luca
 */
public class PrintfInterpolator {
	private Function<Event, String>[] functions;

	public PrintfInterpolator(String value) {
		Pattern p = Pattern.compile("%\\{(\\w*)\\}");
		Matcher m = p.matcher(value);
		int lastMatchEnd = 0;
		List<Function<Event,String>> funcs = new ArrayList<>();
		while(m.find()) {
			int start = m.start();
			int end = m.end();
			if(lastMatchEnd != start) {
				String fixedString = value.substring(lastMatchEnd, start);
				funcs.add(e->fixedString);
			}
			String attrName = m.group(1);
			funcs.add(e->extracted(e, attrName));
			lastMatchEnd = end;
		}
		if(lastMatchEnd != value.length()) {
			String fixedString = value.substring(lastMatchEnd, value.length());
			funcs.add(e->fixedString);
		}
		this.functions = funcs.toArray(Function[]::new);
	}

	PrintfInterpolator(Function<Event,String>[] functions) {
		this.functions = functions;
	}

	private String extracted(Event e, String attrName) {
		Object attribute = e.getField(attrName);
		return attribute == null ? "" : attribute.toString();
	}

	public String interpolate(Event e) {
		StringBuilder builder = new StringBuilder();
		for(var func:functions) {
			builder.append(func.apply(e));
		}
		return builder.toString();
	}
	
}