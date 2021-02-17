package it.foosoft.shipper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestPrintfInterpolator {
	@Test
	public void test1() {
		PrintfInterpolator interpolator = new PrintfInterpolator("%{field1}");
		EventImpl evt = new EventImpl().withField("field1", "value1");
		assertEquals("value1", interpolator.interpolate(evt));
	}

	@Test
	public void test2() {
		PrintfInterpolator interpolator = new PrintfInterpolator("before %{field1} after");
		EventImpl evt = new EventImpl().withField("field1", "value1");
		assertEquals("before value1 after", interpolator.interpolate(evt));
	}

	// Don't really know what to do here... throwing?  
	@Test
	public void testEvaluateOnNull() {
		PrintfInterpolator interpolator = new PrintfInterpolator("before %{field1} after");
		EventImpl evt = new EventImpl();
		assertEquals("before  after", interpolator.interpolate(evt));
	}
	
	// TODO: is there any such thing as ${[aaa][bbb]} ?
}
