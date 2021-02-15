package it.foosoft.shipper.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.core.EventImpl;

public class TestMutateFilter {
	@Test
	public void testRenameSimple() {
		Event e = new EventImpl();
		e.setField("test", 10);
		MutateFilter m = new MutateFilter();
		m.rename = Map.of("test","tost");
		m.start();
		m.process(e);
		assertEquals(10, e.getField("tost"));
	}
	@Test
	public void testRenameBrackets() {
		Event e = new EventImpl();
		e.setField("test", 10);
		MutateFilter m = new MutateFilter();
		m.rename = Map.of("test","[tost]");
		m.start();
		m.process(e);
		assertEquals(10, e.getField("tost"));
	}
	@Test
	public void testRenameNested() {
		Event e = new EventImpl();
		e.setField("test", 10);
		MutateFilter m = new MutateFilter();
		m.rename = Map.of("test","[tost][test]");
		m.start();
		m.process(e);
		assertTrue(e.getField("tost") instanceof Map);
		assertEquals(10, ((Map)e.getField("tost")).get("test"));
	}
}
