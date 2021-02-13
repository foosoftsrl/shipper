package it.foosoft.shipper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.foosoft.shipper.api.Bag;

public class TestBagImpl {
	@Test
	public void testSerialization() throws JsonProcessingException {
		BagImpl bag = new BagImpl();
		bag.setNumericProperty("int", 1);
		bag.setStringProperty("string", "2");
		BagImpl childBag = new BagImpl();
		childBag.setNumericProperty("int", 3);
		childBag.setStringProperty("string", "4");
		bag.setBagProperty("bag", childBag);
		String s = new ObjectMapper().writeValueAsString(bag);
		assertEquals("{\"string\":\"2\",\"bag\":{\"string\":\"4\",\"int\":3},\"int\":1}", s);
	}

	@Test
	public void testDeserialization() throws JsonProcessingException {
		BagImpl bag = new ObjectMapper().readValue("{\"string\":\"2\",\"bag\":{\"string\":\"4\",\"int\":3},\"int\":1}", BagImpl.class);
		assertEquals(1, bag.getNumericProperty("int"));
		assertEquals("2", bag.getStringProperty("string"));
		Bag childBag = bag.getBagProperty("bag");
		assertNotNull(childBag);
		assertEquals(3, childBag.getNumericProperty("int"));
		assertEquals("4", childBag.getStringProperty("string"));
	}
}
