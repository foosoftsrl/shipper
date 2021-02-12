package it.foosoft.shipper.plugins;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.core.InputContextImpl;

public class TestFileInput {

	@TempDir
	File tempDir;

	/**
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void test() throws IOException, InterruptedException, ExecutionException {
		List<Event> events = new ArrayList<>();
		FileInput fileInput = new FileInput(new InputContextImpl(evt->{
			events.add(evt);
		}));

		fileInput.path = tempDir.getAbsolutePath();
		Files.writeString(Paths.get(tempDir.getAbsolutePath(), "test1"), "aaa\nbbb\n");
		Files.writeString(Paths.get(tempDir.getAbsolutePath(), "test2"), "ccc\nddd\n");

		fileInput.start();
		fileInput.forceScan();
		fileInput.stopAndCompleteQueuedJobs();
		assertEquals(0, tempDir.listFiles().length);
		assertEquals(4, events.size());
		Set<String> msgs = events.stream().map(e->e.getFieldAsString("message")).collect(Collectors.toSet());
		assertTrue(msgs.contains("aaa"));
		assertTrue(msgs.contains("bbb"));
		assertTrue(msgs.contains("ccc"));
		assertTrue(msgs.contains("ddd"));
	}

}
