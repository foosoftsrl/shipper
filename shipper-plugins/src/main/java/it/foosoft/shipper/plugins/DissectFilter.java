package it.foosoft.shipper.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.EventProcessor;
import it.foosoft.shipper.api.Filter;
import it.foosoft.shipper.api.Param;
import it.foosoft.shipper.plugins.converters.Converters;

public class DissectFilter implements Filter {

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(DissectFilter.class);
	@NotNull
	public Map<String,String> mapping;

	@Param
	public String tag_on_failure;

	@Param
	public Map<String, String> convert_datatype = new HashMap<>();
	
	public List<EventProcessor> converters = new ArrayList<>();
	
	static class Context {
		Pattern pattern;
		List<String> fields = new ArrayList<>();
	}

	private Map<String,Context> contexts = new HashMap<>();

	@Override
	public boolean process(Event e) {
		boolean successful = true;
		for(var pattern: contexts.entrySet()) {
			Object attr = e.getField(pattern.getKey());
			if(!(attr instanceof String)) {
				LOG.info("Unsupported field type for " + pattern.getKey());
				successful = false;
				continue;
			}
			String attrString = (String)attr;
			Context context = pattern.getValue();

			Matcher m = context.pattern.matcher(attrString);
			if(m.matches()) {
				for(int i = 0; i < context.fields.size(); i++) {
					e.setField(context.fields.get(i), m.group(i + 1));
				}
			} else {
				successful = false;
			}
		}

		//mmhhh... what if a converter fails?
		for(var converter: converters) {
			converter.process(e);
		}
		if(!successful) {
			if(tag_on_failure != null) {
				e.addTag(tag_on_failure);
			}
		}
		return successful;
	}

	@Override
	public void start() {
		prepareContexts();
		prepareConverters();
	}

	private void prepareConverters() {
		converters.clear();
		for(Map.Entry<String, String> entry: convert_datatype.entrySet()) {
			converters.add(Converters.createConverter(entry.getKey(), entry.getValue()));
		}
	}

	private void prepareContexts() {
		Pattern p = Pattern.compile("%\\{(\\?)?(\\w*)\\}"); 
		for(Entry<String, String> mappingEntry: mapping.entrySet()) {
			StringBuilder regexBuilder = new StringBuilder();
			Context ctx = new Context();
			String pattern = mappingEntry.getValue();
			Matcher m = p.matcher(pattern);
			boolean lastMatchIsCaptured = false;
			MatchResult lastMatch = null;
			while(m.find()) {
				String fieldName = m.group(2);
				if(lastMatch != null) {
					String firstChar = "" + pattern.charAt(lastMatch.end()); 
					if(firstChar.equals("\t"))
						firstChar = "\\t";
					if(lastMatchIsCaptured) {
						regexBuilder.append("(");
					}
					regexBuilder.append("[^").append(firstChar).append("]+");
					if(lastMatchIsCaptured)
						regexBuilder.append(")");
				}
				regexBuilder.append(pattern, lastMatch == null ? 0 : lastMatch.end(), m.start());
				lastMatch = m.toMatchResult();
				lastMatchIsCaptured = m.group(1) == null;
				if(lastMatchIsCaptured)
					ctx.fields.add(fieldName);
			}
			if(lastMatch != null) {
				// TODO: Not handling correctly the case when the pattern does not end with a string 
				if(lastMatchIsCaptured) {
					regexBuilder.append("(");
				}
				// if pattern terminates with a field, it is greedy
				if(lastMatch.end() == pattern.length()) {
					regexBuilder.append(".*");
				} else {
					regexBuilder.append("[^").append(pattern.charAt(lastMatch.end())).append("]+");
				}
				if(lastMatchIsCaptured)
					regexBuilder.append(")");
				regexBuilder.append(pattern, lastMatch.end(), pattern.length());
			}
			regexBuilder.append("$");
			ctx.pattern = Pattern.compile(regexBuilder.toString());
			contexts.put(mappingEntry.getKey(), ctx);
		}
		
	}

	@Override
	public void stop() {
	}
}
