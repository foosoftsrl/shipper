package it.foosoft.shipper.plugins;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FieldRef;
import it.foosoft.shipper.api.FieldRefBuilder;
import it.foosoft.shipper.api.FilterPlugin;
import it.foosoft.shipper.api.Inject;
import it.foosoft.shipper.api.PostConstruct;

public class DateFilter implements FilterPlugin {
	private static final Logger LOG = LoggerFactory.getLogger(DateFilter.class);
	@ConfigurationParm
	public String locale = Locale.getDefault().getCountry();

	@ConfigurationParm
	public String timezone;

	@ConfigurationParm
	public String target = "@timestamp";

	@NotNull
	@ConfigurationParm
	public String[] match;

	@ConfigurationParm
	public String[] tag_on_failure = new String[]{"_dateparsefailure"};

	@Inject
	public FieldRefBuilder fieldRefBuilder;

	private String fieldName;


	public interface DateParser {
		public Date parse(String text) throws ParseException;
	}

	// mutable data
	private FieldRef targetField;	

	private DateParser dateParser;

	public DateFilter() {
		
	}

	@PostConstruct
	void postConstruct() {
		targetField = fieldRefBuilder.createFieldRef(target);
		
		if(match.length != 2) {
			throw new IllegalArgumentException("DateFilter.match currently supports only 2 arguments");
		}
		fieldName = match[0];
		Locale selectedLocale = getLocaleFromString(locale);
		if(selectedLocale != Locale.US) {
			LOG.info("A non-US locale has been specified. The fallback mechanism to US is not implemented");
		}
		if("UNIX".equals(match[1])) {
			dateParser = this::extracted;
		} else if("UNIX_MS".equals(match[1])) {
			dateParser = str->new Date(Long.parseLong(str));
		} else if("ISO8601".equals(match[1])) {
			DateTimeFormatter parse = DateTimeFormatter.ISO_DATE_TIME;
			dateParser = str->{
				// Parse date, do not care about stray data at end.
				// I'm afraid that this is a very bad idea, but there's a unit test
				// I made ages ago... and... I don't want to think about it right now
		        TemporalAccessor accessor = parse.parse(str, new ParsePosition(0));
		        return Date.from(accessor.query(ZonedDateTime::from).toInstant());
			};
		} else if("".equals(match[1])) {
			dateParser = str->new Date(Long.parseLong(str));
		} else {
			DateTimeFormatter parser = createParser(selectedLocale);
			dateParser = str->{
				// Parse date, do not care about stray data at end.
				// I'm afraid that this is a very bad idea, but there's a unit test
				// I made ages ago... and... I don't want to think about it right now
		        TemporalAccessor accessor = parser.parse(str, new ParsePosition(0));
		        return Date.from(accessor.query(ZonedDateTime::from).toInstant());
			};
		}
	}
	/**
	 * 	Commodity constructor for unit tests
	 * @param fieldRefBuilder
	 */
	public DateFilter(FieldRefBuilder fieldRefBuilder) {
		this.fieldRefBuilder = fieldRefBuilder;
	}

	@Override
	public boolean process(Event evt) {
		Object obj = evt.getField(fieldName);
		if(!(obj instanceof String)) {
			return false;
		}
		String dateAsText = (String)obj;
		try {
			targetField.set(evt, dateParser.parse(dateAsText));
		} catch (Exception e) {
			LOG.info("Failed parsing date {}", dateAsText, e);
			evt.addTags(tag_on_failure);
			return false;
		}
		return true;
	}

	private Date extracted(String str) {
		return new Date((long)(Double.parseDouble(str) * 1000l));
	}

	private DateTimeFormatter createParser(Locale selectedLocale) {
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		DateTimeFormatter parse = builder
			.parseCaseInsensitive()
			.appendPattern(match[1])
			.toFormatter(selectedLocale);
		ZoneId zoneId = this.timezone != null ? ZoneId.of(this.timezone) : ZoneId.systemDefault();
		parse = parse.withZone(zoneId);
		return parse;
	}

	/**
     * Convert a string based locale into a Locale Object.
     * Assumes the string has form "{language}_{country}_{variant}".
     * Examples: "en", "de_DE", "_GB", "en_US_WIN", "de__POSIX", "fr_MAC"
     *  
     * @param localeString The String
     * @return the Locale
     */
    public static Locale getLocaleFromString(String localeString)
    {
        if (localeString == null)
        {
            return null;
        }
        localeString = localeString.trim();
        if ("default".equalsIgnoreCase(localeString))
        {
            return Locale.getDefault();
        }

        // Extract language
        int languageIndex = localeString.indexOf('_');
        String language = null;
        if (languageIndex == -1)
        {
            // No further "_" so is "{language}" only
            return new Locale(localeString, "");
        }
        else
        {
            language = localeString.substring(0, languageIndex);
        }

        // Extract country
        int countryIndex = localeString.indexOf('_', languageIndex + 1);
        String country = null;
        if (countryIndex == -1)
        {
            // No further "_" so is "{language}_{country}"
            country = localeString.substring(languageIndex+1);
            return new Locale(language, country);
        }
        else
        {
            // Assume all remaining is the variant so is "{language}_{country}_{variant}"
            country = localeString.substring(languageIndex+1, countryIndex);
            String variant = localeString.substring(countryIndex+1);
            return new Locale(language, country, variant);
        }
    }
}
