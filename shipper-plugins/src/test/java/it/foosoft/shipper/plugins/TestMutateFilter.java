package it.foosoft.shipper.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
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
		assertNull(e.getField("test"));
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
		assertNull(e.getField("test"));
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
		assertNull(e.getField("test"));
	}

	@Test
	public void testRenameNestedSrc() {
		Event e = new EventImpl();
		e.setField("test", new HashMap<>(Map.of("test", 10)));
		MutateFilter m = new MutateFilter();
		m.rename = Map.of("[test][test]","[tost][test]");
		m.start();
		m.process(e);
		assertTrue(e.getField("tost") instanceof Map);
		assertEquals(10, ((Map)e.getField("tost")).get("test"));
	}

	@Test
	public void testCopySimple() {
		Event e = new EventImpl();
		e.setField("test", 10);
		MutateFilter m = new MutateFilter();
		m.copy = Map.of("test","tost");
		m.start();
		m.process(e);
		assertEquals(10, e.getField("tost"));
		assertEquals(10, e.getField("test"));
	}

	@Test
	public void testCopyBrackets() {
		Event e = new EventImpl();
		e.setField("test", 10);
		MutateFilter m = new MutateFilter();
		m.copy = Map.of("test","[tost]");
		m.start();
		m.process(e);
		assertEquals(10, e.getField("tost"));
		assertEquals(10, e.getField("test"));
	}
	@Test
	public void testCopyNested() {
		Event e = new EventImpl();
		e.setField("test", 10);
		MutateFilter m = new MutateFilter();
		m.copy = Map.of("test","[tost][test]");
		m.start();
		m.process(e);
		assertTrue(e.getField("tost") instanceof Map);
		assertEquals(10, ((Map)e.getField("tost")).get("test"));
		assertEquals(10, e.getField("test"));
	}

	@Test
	public void testCopyNestedSrc() {
		Event e = new EventImpl();
		e.setField("test", new HashMap<>(Map.of("test", 10)));
		MutateFilter m = new MutateFilter();
		m.copy = Map.of("[test][test]","[tost][test]");
		m.start();
		m.process(e);
		assertTrue(e.getField("tost") instanceof Map);
		assertEquals(10, ((Map)e.getField("tost")).get("test"));
	}
}
