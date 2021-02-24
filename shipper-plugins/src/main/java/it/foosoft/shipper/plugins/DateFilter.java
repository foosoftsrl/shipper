package it.foosoft.shipper.plugins;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FilterPlugin;

public class DateFilter implements FilterPlugin {
	private static final Logger LOG = LoggerFactory.getLogger(DateFilter.class);
	@ConfigurationParm
	public String locale = Locale.getDefault().getCountry();

	@ConfigurationParm
	public String timezone;

	@NotNull
	@ConfigurationParm
	public String[] match;

	private String fieldName;

	ThreadLocal<SimpleDateFormat> dateParser;	

	@Override
	public boolean process(Event evt) {
		Object obj = evt.getField(fieldName);
		if(!(obj instanceof String)) {
			return false;
		}
		String dateAsText = (String)obj;
		try {
			Date d = dateParser.get().parse(dateAsText);
			evt.setTimestamp(d);
		} catch (Exception e) {
			LOG.info("Failed parsing date {}", dateAsText);
			return false;
		}
		return true;
	}

	@Override
	public void start() {
		if(match.length != 2) {
			throw new IllegalArgumentException("DateFilter.match currently supports only 2 arguments");
		}
		fieldName = match[0];
		Locale selectedLocale = getLocaleFromString(locale);
		dateParser = ThreadLocal.withInitial(() -> new SimpleDateFormat(match[1], selectedLocale));		
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
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
        if (localeString.toLowerCase().equals("default"))
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
