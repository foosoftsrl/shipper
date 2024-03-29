package it.foosoft.shipper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestStringInterpolator {
	@Test
	public void test1() {
		StringInterpolator interpolator = new StringInterpolator("%{field1}");
		EventImpl evt = new EventImpl().withField("field1", "value1");
		assertEquals("value1", interpolator.evaluate(evt));
	}

	@Test
	public void test2() {
		StringInterpolator interpolator = new StringInterpolator("before %{field1} after");
		EventImpl evt = new EventImpl().withField("field1", "value1");
		assertEquals("before value1 after", interpolator.evaluate(evt));
	}

	// Don't really know what to do here... throwing?  
	@Test
	public void testEvaluateOnNull() {
		StringInterpolator interpolator = new StringInterpolator("before %{field1} after");
		EventImpl evt = new EventImpl();
		assertEquals("before null after", interpolator.evaluate(evt));
	}
	
	// TODO: is there any such thing as ${[aaa][bbb]} ?
}
