package it.foosoft.shipper.plugins.geoip;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.db.CHMCache;
import com.maxmind.db.InvalidDatabaseException;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.IspResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

import it.foosoft.shipper.api.Event;

public class GeoIPFilter {
  private static Logger logger = LoggerFactory.getLogger(GeoIPFilter.class);
  // The free GeoIP2 databases
  private static final String CITY_LITE_DB_TYPE = "GeoLite2-City";
  private static final String COUNTRY_LITE_DB_TYPE = "GeoLite2-Country";
  private static final String ASN_LITE_DB_TYPE = "GeoLite2-ASN";

  // The paid GeoIP2 databases
  private static final String CITY_DB_TYPE = "GeoIP2-City";
  private static final String CITY_AFRICA_DB_TYPE = "GeoIP2-City-Africa";
  private static final String CITY_ASIA_PACIFIC_DB_TYPE = "GeoIP2-City-Asia-Pacific";
  private static final String CITY_EUROPE_DB_TYPE = "GeoIP2-City-Europe";
  private static final String CITY_NORTH_AMERICA_DB_TYPE = "GeoIP2-City-North-America";
  private static final String CITY_SOUTH_AMERICA_DB_TYPE = "GeoIP2-City-South-America";
  private static final String COUNTRY_DB_TYPE = "GeoIP2-Country";
  private static final String ISP_DB_TYPE = "GeoIP2-ISP";

  private final String sourceField;
  private final String targetField;
  private final Set<Fields> desiredFields;
  private final DatabaseReader databaseReader;

  public GeoIPFilter(String sourceField, String targetField, List<String> fields, String databasePath, int cacheSize) {
    this.sourceField = sourceField;
    this.targetField = targetField;
    final File databaseFile = new File(databasePath);
    try {
      this.databaseReader = new DatabaseReader.Builder(databaseFile).withCache(new CHMCache(cacheSize)).build();
    } catch (InvalidDatabaseException e) {
      throw new IllegalArgumentException("The database provided is invalid or corrupted.", e);
    } catch (IOException e) {
      throw new IllegalArgumentException("The database provided was not found in the path", e);
    }
    this.desiredFields = createDesiredFields(fields);
  }

  private Set<Fields> createDesiredFields(List<String> fields) {
    Set<Fields> desiredFields = EnumSet.noneOf(Fields.class);
    if (fields == null || fields.isEmpty()) {
      switch (databaseReader.getMetadata().getDatabaseType()) {
        case CITY_LITE_DB_TYPE:
        case CITY_DB_TYPE:
        case CITY_AFRICA_DB_TYPE:
        case CITY_ASIA_PACIFIC_DB_TYPE:
        case CITY_EUROPE_DB_TYPE:
        case CITY_NORTH_AMERICA_DB_TYPE:
        case CITY_SOUTH_AMERICA_DB_TYPE:
          desiredFields = Fields.DEFAULT_CITY_FIELDS;
          break;
        case COUNTRY_LITE_DB_TYPE:
        case COUNTRY_DB_TYPE:
          desiredFields = Fields.DEFAULT_COUNTRY_FIELDS;
          break;
        case ISP_DB_TYPE:
          desiredFields = Fields.DEFAULT_ISP_FIELDS;
          break;
        case ASN_LITE_DB_TYPE:
          desiredFields = Fields.DEFAULT_ASN_LITE_FIELDS;
          break;
      }
    } else {
      for (String fieldName : fields) {
        desiredFields.add(Fields.parseField(fieldName));
      }
    }
    return desiredFields;
  }

  public boolean handleEvent(Event event) {
    Object input = event.getField(sourceField);
    if (input == null) {
      return false;
    }
    String ip;

    if (input instanceof List) {
      ip = (String) ((List) input).get(0);
    } else if (input instanceof String) {
      ip = (String) input;
    } else {
      throw new IllegalArgumentException("Expected input field value to be String or List type");
    }

    if (ip.trim().isEmpty()) {
      return false;
    }

    Map<String, Object> geoData = new HashMap<>();
    Object previousValue = event.getField(targetField);
    try {
      final InetAddress ipAddress = InetAddress.getByName(ip);
      switch (databaseReader.getMetadata().getDatabaseType()) {
        case CITY_LITE_DB_TYPE:
        case CITY_DB_TYPE:
        case CITY_AFRICA_DB_TYPE:
        case CITY_ASIA_PACIFIC_DB_TYPE:
        case CITY_EUROPE_DB_TYPE:
        case CITY_NORTH_AMERICA_DB_TYPE:
        case CITY_SOUTH_AMERICA_DB_TYPE:
          geoData = retrieveCityGeoData(ipAddress);
          break;
        case COUNTRY_LITE_DB_TYPE:
        case COUNTRY_DB_TYPE:
          geoData = retrieveCountryGeoData(ipAddress);
          break;
        case ASN_LITE_DB_TYPE:
          geoData = retrieveAsnGeoData(ipAddress);
          break;
        case ISP_DB_TYPE:
          geoData = retrieveIspGeoData(ipAddress);
          break;
        default:
          throw new IllegalStateException("Unsupported database type " + databaseReader.getMetadata().getDatabaseType() + "");
      }
    } catch (UnknownHostException e) {
      logger.debug("IP Field contained invalid IP address or hostname. exception={}, field={}, event={}", e, sourceField, event);
    } catch (AddressNotFoundException e) {
      logger.debug("IP not found! exception={}, field={}, event={}", e, sourceField, event);
    } catch (GeoIp2Exception | IOException e) {
      logger.debug("GeoIP2 Exception. exception={}, field={}, event={}", e, sourceField, event);
    }

    if(previousValue instanceof Map) {
    	Map previousMap = (Map)previousValue;
    	geoData.putAll(previousMap);
    }

    event.setField(targetField, geoData);
    return true;
  }

  private Map<String,Object> retrieveCityGeoData(InetAddress ipAddress) throws GeoIp2Exception, IOException {
    CityResponse response = databaseReader.city(ipAddress);
    Country country = response.getCountry();
    City city = response.getCity();
    Location location = response.getLocation();
    Continent continent = response.getContinent();
    Postal postal = response.getPostal();
    Subdivision subdivision = response.getMostSpecificSubdivision();
    Map<String, Object> geoData = new HashMap<>();

    // if location is empty, there is no point populating geo data
    // and most likely all other fields are empty as well
    if (location.getLatitude() == null && location.getLongitude() == null) {
      return geoData;
    }

    for (Fields desiredField : this.desiredFields) {
      switch (desiredField) {
        case CITY_NAME:
          String cityName = city.getName();
          if (cityName != null) {
            geoData.put(Fields.CITY_NAME.fieldName(), cityName);
          }
          break;
        case CONTINENT_CODE:
          String continentCode = continent.getCode();
          if (continentCode != null) {
            geoData.put(Fields.CONTINENT_CODE.fieldName(), continentCode);
          }
          break;
        case CONTINENT_NAME:
          String continentName = continent.getName();
          if (continentName != null) {
            geoData.put(Fields.CONTINENT_NAME.fieldName(), continentName);
          }
          break;
        case COUNTRY_NAME:
          String countryName = country.getName();
          if (countryName != null) {
            geoData.put(Fields.COUNTRY_NAME.fieldName(), countryName);
          }
          break;
        case COUNTRY_CODE2:
          String countryCode2 = country.getIsoCode();
          if (countryCode2 != null) {
            geoData.put(Fields.COUNTRY_CODE2.fieldName(), countryCode2);
          }
          break;
        case COUNTRY_CODE3:
          String countryCode3 = country.getIsoCode();
          if (countryCode3 != null) {
            geoData.put(Fields.COUNTRY_CODE3.fieldName(), countryCode3);
          }
          break;
        case IP:
          geoData.put(Fields.IP.fieldName(), ipAddress.getHostAddress());
          break;
        case POSTAL_CODE:
          String postalCode = postal.getCode();
          if (postalCode != null) {
            geoData.put(Fields.POSTAL_CODE.fieldName(), postalCode);
          }
          break;
        case DMA_CODE:
          Integer dmaCode = location.getMetroCode();
          if (dmaCode != null) {
            geoData.put(Fields.DMA_CODE.fieldName(), dmaCode);
          }
          break;
        case REGION_NAME:
          String subdivisionName = subdivision.getName();
          if (subdivisionName != null) {
            geoData.put(Fields.REGION_NAME.fieldName(), subdivisionName);
          }
          break;
        case REGION_CODE:
          String subdivisionCode = subdivision.getIsoCode();
          if (subdivisionCode != null) {
            geoData.put(Fields.REGION_CODE.fieldName(), subdivisionCode);
          }
          break;
        case TIMEZONE:
          String locationTimeZone = location.getTimeZone();
          if (locationTimeZone != null) {
            geoData.put(Fields.TIMEZONE.fieldName(), locationTimeZone);
          }
          break;
        case LOCATION:
          Double latitude = location.getLatitude();
          Double longitude = location.getLongitude();
          if (latitude != null && longitude != null) {
            Map<String, Object> locationObject = new HashMap<>();
            locationObject.put("lat", latitude);
            locationObject.put("lon", longitude);
            geoData.put(Fields.LOCATION.fieldName(), locationObject);
          }
          break;
        case LATITUDE:
          Double lat = location.getLatitude();
          if (lat != null) {
            geoData.put(Fields.LATITUDE.fieldName(), lat);
          }
          break;
        case LONGITUDE:
          Double lon = location.getLongitude();
          if (lon != null) {
            geoData.put(Fields.LONGITUDE.fieldName(), lon);
          }
          break;
      }
    }

    return geoData;
  }

  private Map<String,Object> retrieveCountryGeoData(InetAddress ipAddress) throws GeoIp2Exception, IOException {
    CountryResponse response = databaseReader.country(ipAddress);
    Country country = response.getCountry();
    Continent continent = response.getContinent();
    Map<String, Object> geoData = new HashMap<>();

    for (Fields desiredField : this.desiredFields) {
      switch (desiredField) {
        case IP:
          geoData.put(Fields.IP.fieldName(), ipAddress.getHostAddress());
          break;
        case COUNTRY_CODE2:
          String countryCode2 = country.getIsoCode();
          if (countryCode2 != null) {
            geoData.put(Fields.COUNTRY_CODE2.fieldName(), countryCode2);
          }
          break;
        case COUNTRY_NAME:
          String countryName = country.getName();
          if (countryName != null) {
            geoData.put(Fields.COUNTRY_NAME.fieldName(), countryName);
          }
          break;
        case CONTINENT_NAME:
          String continentName = continent.getName();
          if (continentName != null) {
            geoData.put(Fields.CONTINENT_NAME.fieldName(), continentName);
          }
          break;
      }
    }

    return geoData;
  }

  private Map<String, Object> retrieveIspGeoData(InetAddress ipAddress) throws GeoIp2Exception, IOException {
    IspResponse response = databaseReader.isp(ipAddress);

    Map<String, Object> geoData = new HashMap<>();
    for (Fields desiredField : this.desiredFields) {
      switch (desiredField) {
        case IP:
          geoData.put(Fields.IP.fieldName(), ipAddress.getHostAddress());
          break;
        case AUTONOMOUS_SYSTEM_NUMBER:
          Long asn = response.getAutonomousSystemNumber();
          if (asn != null) {
            geoData.put(Fields.AUTONOMOUS_SYSTEM_NUMBER.fieldName(), asn);
          }
          break;
        case AUTONOMOUS_SYSTEM_ORGANIZATION:
          String aso = response.getAutonomousSystemOrganization();
          if (aso != null) {
            geoData.put(Fields.AUTONOMOUS_SYSTEM_ORGANIZATION.fieldName(), aso);
          }
          break;
        case ISP:
          String isp = response.getIsp();
          if (isp != null) {
            geoData.put(Fields.ISP.fieldName(), isp);
          }
          break;
        case ORGANIZATION:
          String org = response.getOrganization();
          if (org != null) {
            geoData.put(Fields.ORGANIZATION.fieldName(), org);
          }
          break;
      }
    }

    return geoData;
  }

  private Map<String, Object> retrieveAsnGeoData(InetAddress ipAddress) throws GeoIp2Exception, IOException {
    AsnResponse response = databaseReader.asn(ipAddress);
    Map<String, Object> geoData = new HashMap<>();
    for (Fields desiredField : this.desiredFields) {
      switch (desiredField) {
        case IP:
          geoData.put(Fields.IP.fieldName(), ipAddress.getHostAddress());
          break;
        case AUTONOMOUS_SYSTEM_NUMBER:
          Long asn = response.getAutonomousSystemNumber();
          if (asn != null) {
            geoData.put(Fields.AUTONOMOUS_SYSTEM_NUMBER.fieldName(), asn);
          }
          break;
        case AUTONOMOUS_SYSTEM_ORGANIZATION:
          String aso = response.getAutonomousSystemOrganization();
          if (aso != null) {
            geoData.put(Fields.AUTONOMOUS_SYSTEM_ORGANIZATION.fieldName(), aso);
          }
          break;
      }
    }

    return geoData;
  }
}