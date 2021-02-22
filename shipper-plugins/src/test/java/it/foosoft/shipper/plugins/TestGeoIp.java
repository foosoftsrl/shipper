package it.foosoft.shipper.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import it.foosoft.shipper.core.EventImpl;

public class TestGeoIp {

	@Test
	public void testLocal() throws URISyntaxException {
		GeoIpFilter f = new GeoIpFilter();
		f.source = "ip";
		f.database = new File(getClass().getResource("files/GeoLite2-ASN.mmdb").toURI()).getAbsolutePath();
		f.start();
		EventImpl evt = new EventImpl().withField("ip", "127.0.0.1");
		f.process(evt);
		assertTrue(evt.getField("geoip") instanceof Map);
		Map geoIp = (Map)evt.getField("geoip");
		assertEquals(0, geoIp.entrySet().size());
	}

	@Test
	public void testNonLocal() throws URISyntaxException {
		GeoIpFilter f = new GeoIpFilter();
		f.source = "ip";
		f.database = new File(getClass().getResource("files/GeoLite2-ASN.mmdb").toURI()).getAbsolutePath();
		f.start();
		EventImpl evt = new EventImpl().withField("ip", "8.8.8.8");
		f.process(evt);
		assertTrue(evt.getField("geoip") instanceof Map);
		Map geoIp = (Map)evt.getField("geoip");
		assertEquals(15169, geoIp.get("asn"));
	}
}
