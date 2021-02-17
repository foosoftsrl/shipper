package it.foosoft.shipper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import it.foosoft.shipper.api.FileWalker;

public class TestWalker {

	@TempDir
	File tempDir;
	
	@Test
	public void testFindDir() throws IOException {
		assertEquals(1, FileWalker.walk(tempDir.getAbsolutePath()).size());
	}

	@Test
	public void testSimple() throws IOException {
		File path = new File(tempDir, "test1");
		File path2 = new File(tempDir, "test2");
		File path3 = new File(tempDir, "test3.txt");
		Files.writeString(path.toPath(), "a");
		Files.writeString(path2.toPath(), "b");
		Files.writeString(path3.toPath(), "c");
		
		assertEquals(3, FileWalker.walk(tempDir.getAbsolutePath() + "/*").size());
		assertEquals(2, FileWalker.walk(tempDir.getAbsolutePath() + "/{test1,test2}").size());
		assertEquals(1, FileWalker.walk(tempDir.getAbsolutePath() + "/test3.txt").size());
		assertEquals(1, FileWalker.walk(tempDir.getAbsolutePath() + "/{test3.txt}").size());
		assertEquals(1, FileWalker.walk(tempDir.getAbsolutePath() + "/*.txt").size());
	}

	@Test
	public void test() throws IOException {
		File path = new File(tempDir, "test1.txt");
		File path2 = new File(tempDir, "test2.txt");
		File path3 = new File(tempDir, "a/b/c.txt");
		path3.getParentFile().mkdirs();
		Files.writeString(path.toPath(), "aaa\nbbb\n");
		Files.writeString(path2.toPath(), "ccc\nddd\n");
		Files.writeString(path3.toPath(), "eee\nfff\n");
		assertEquals(1, FileWalker.walk(tempDir.getAbsolutePath() + "/**/*.txt").size());
		assertEquals(3, FileWalker.walk(tempDir.getAbsolutePath() + "/**.txt").size());
		assertEquals(2, FileWalker.walk(tempDir.getAbsolutePath() + "/t**.txt").size());
		assertEquals(2, FileWalker.walk(tempDir.getAbsolutePath() + "/t**txt").size());
		assertEquals(1, FileWalker.walk(tempDir.getAbsolutePath() + "/**c.txt").size());
	}
}
