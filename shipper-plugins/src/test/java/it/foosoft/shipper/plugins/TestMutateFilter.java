package it.foosoft.shipper.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.core.EventImpl;
import it.foosoft.shipper.core.FieldRefBuilderImpl;

public class TestMutateFilter {
	
	@Test
	public void testRenameSimple() {
		Event e = new EventImpl();
		e.setField("test", 10);
		MutateFilter m = createMutateFilter();
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
		MutateFilter m = createMutateFilter();
		m.rename = Map.of("test","[tost]");
		m.start();
		m.process(e);
		assertEquals(10, e.getField("tost"));
		assertNull(e.getField("test"));
	}
	private MutateFilter createMutateFilter() {
		MutateFilter mutateFilter = new MutateFilter();
		mutateFilter.fieldRefBuilder = new FieldRefBuilderImpl();
		return mutateFilter;
	}
	@Test
	public void testRenameNested() {
		Event e = new EventImpl();
		e.setField("test", 10);
		MutateFilter m = createMutateFilter();
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
		MutateFilter m = createMutateFilter();
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
		MutateFilter m = createMutateFilter();
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
		MutateFilter m = createMutateFilter();
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
		MutateFilter m = createMutateFilter();
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
		MutateFilter m = createMutateFilter();
		m.copy = Map.of("[test][test]","[tost][test]");
		m.start();
		m.process(e);
		assertTrue(e.getField("tost") instanceof Map);
		assertEquals(10, ((Map)e.getField("tost")).get("test"));
	}

	@Test
	public void testGsub() {
		Event e = new EventImpl();
		e.setField("test", "test");
		MutateFilter m = createMutateFilter();
		m.gsub = new String[] {"test", "e", "o"};
		m.start();
		m.process(e);
		assertEquals("tost", e.getField("test"));
	}

	@Test
	public void testSplit() {
		Event e = new EventImpl();
		e.setField("test", "aaa,bbb");
		MutateFilter m = createMutateFilter();
		m.split = Map.of("test",",");
		m.start();
		m.process(e);
		String[] s = (String[])e.getField("test");
		assertEquals(2, s.length);
		assertEquals("aaa", s[0]);
		assertEquals("bbb", s[1]);
	}

	@Test
	public void testReplaceSimple() {
		Event e = new EventImpl();
		e.setField("test", "test");
		MutateFilter m = createMutateFilter();
		m.replace = Map.of("test","replacement");
		m.start();
		m.process(e);
		assertEquals("replacement", e.getField("test"));
	}

	@Test
	public void testReplaceInterpolate() {
		Event e = new EventImpl();
		e.setField("test", "test");
		e.setField("a", "1");
		e.setField("b", "2");
		MutateFilter m = createMutateFilter();
		m.replace = Map.of("test","%{a}-%{b}");
		m.start();
		m.process(e);
		assertEquals("1-2", e.getField("test"));
	}
}
