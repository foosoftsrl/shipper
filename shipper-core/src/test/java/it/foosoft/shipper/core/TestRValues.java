package it.foosoft.shipper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import it.foosoft.shipper.core.rvalue.RValueBuilder;

public class TestRValues {
	@Test
	public void testSimple() {
		EventImpl evt = new EventImpl().withField("f", "v");
		assertEquals("v", RValueBuilder.makeFieldRefRValue("f").get(evt));
	}

	@Test
	public void testComplex() {
		EventImpl evt = new EventImpl().withField("f1", Map.of("f2","v"));
		assertEquals("v", RValueBuilder.makeFieldRefRValue("f1","f2").get(evt));
	}

	@Test
	public void testMetadata() {
		EventImpl evt = new EventImpl().withMetadata("f", "v");
		assertEquals("v", RValueBuilder.makeFieldRefRValue("@metadata","f").get(evt));
	}
}
