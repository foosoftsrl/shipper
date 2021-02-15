package it.foosoft.shipper.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class FileWalker {
	public enum Type {
		SINGLE,
		SIMPLE,
		COMPLEX
	};
	
	public static int ALPHA_SORT;
	
	public static List<Path> walk(String filter) throws IOException {
		List<Path> result = new ArrayList<>();
		walk(filter, result::add);
		return result;
	}

	public static void walk(String filter, Consumer<Path> consumer) throws IOException {
		walk(filter.substring(1), Path.of("/"), consumer);
	}

	public static void walk(String filter, Path path, Consumer<Path> consumer) throws IOException {
		int pos = filter.indexOf("/");
		if(pos == -1) {
			String regex = Globs.toUnixRegexPattern(filter);
			Pattern pattern = Pattern.compile(regex);
			if(Files.isDirectory(path)) {
				for(Path s: Files.newDirectoryStream(path)) {
					if(pattern.matcher(s.getFileName().toString()).matches()) {
						consumer.accept(s);
					}
				}
			}
			return;
		}
		String segmentFilter = filter.substring(0, pos);
		Type type = Type.SINGLE;
		for(int i = 0; i < pos; i++) {
			if(filter.charAt(i) == '*' || filter.charAt(i) == '{') {
				type = Type.SIMPLE; 
			}
		}
		if(type == Type.SINGLE) {
			walk(filter.substring(pos + 1), path.resolve(segmentFilter), consumer);
		} else if(type == Type.SIMPLE) {
			String regex = Globs.toUnixRegexPattern(segmentFilter);
			Pattern pattern = Pattern.compile(regex);
			if(Files.isDirectory(path)) {
				for(Path childPath: Files.newDirectoryStream(path)) {
					if(pattern.matcher(childPath.getFileName().toString()).matches()) {
						walk(filter.substring(pos + 1), childPath, consumer);
					}
				}
			}
		}
	}
	public static void blindWalk(String filter, Path path, Consumer<String> consumer) throws IOException {
		
	}
}
