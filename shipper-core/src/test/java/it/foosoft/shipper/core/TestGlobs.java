package it.foosoft.shipper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import it.foosoft.shipper.api.Globs;

public class TestGlobs {

	@TempDir
	File tempDir;
	
	@Test
	public void testDoubleStar() throws IOException {
		assertEquals("^.*$", Globs.toUnixRegexPattern("**"));
	}
	
	@Test
	public void testSingleStar() {
		assertEquals("^[^/]*$", Globs.toUnixRegexPattern("*"));
	}

}
