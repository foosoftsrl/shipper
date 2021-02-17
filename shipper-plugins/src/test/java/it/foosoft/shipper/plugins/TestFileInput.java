package it.foosoft.shipper.plugins;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.core.InputContextImpl;
import it.foosoft.shipper.plugins.FileInput.Mode;

public class TestFileInput {

	@TempDir
	File tempDir;

	@Test
	public void test() throws IOException, InterruptedException, ExecutionException {
		List<Event> events = new ArrayList<>();
		FileInput fileInput = new FileInput(new InputContextImpl(evt->{
			events.add(evt);
		}));

		fileInput.path = tempDir.getAbsolutePath() + "/*";
		
		File path = new File(tempDir, "test1");
		File path2 = new File(tempDir, "test2");
		Files.writeString(path.toPath(), "aaa\nbbb\n");
		Files.writeString(path2.toPath(), "ccc\nddd\n");

		fileInput.mode = Mode.read;
		fileInput.start();
		fileInput.forceScan();
		
		assertTimeout(Duration.ofMinutes(2), ()->{
			while(path.exists()) {
				Thread.sleep(100);
			}
			while(path2.exists()) {
				Thread.sleep(100);
			}
		});
		fileInput.stop();
		assertEquals(0, tempDir.listFiles().length);
		assertEquals(4, events.size());
		Set<String> msgs = events.stream().map(e->e.getFieldAsString("message")).collect(Collectors.toSet());
		assertTrue(msgs.contains("aaa"));
		assertTrue(msgs.contains("bbb"));
		assertTrue(msgs.contains("ccc"));
		assertTrue(msgs.contains("ddd"));
	}

}
