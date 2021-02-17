package it.foosoft.shipper.api;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileWalker {
	public enum Type {
		SINGLE,
		SIMPLE,
		COMPLEX
	};
	
	public static int ALPHA_SORT;
	
	public interface Consumer {
		public void consume(Path path);
	}
	
	public static List<Path> walk(String filter) throws IOException {
		List<Path> result = new ArrayList<>();
		walk(filter, result::add);
		return result;
	}

	public static void walk(String filter, Consumer consumer) throws IOException {
		filter = filter.replace("\\", "/");
		if(!isAbsolutePath(filter)) {
			filter = Paths.get("").toString().replace("\\", "/") + filter;
		}
		int slashPos = filter.indexOf("/");
		if(slashPos < 0)
			throw new IllegalStateException("Can't find first slash, this is an internal error");
		walk(filter.substring(slashPos + 1), Path.of(filter.substring(0, slashPos + 1)), consumer);
	}

	private static boolean isAbsolutePath(String filter) {
		return filter.contains(":") || filter.startsWith("/");
	}

	public static void walk(String filter, Path path, Consumer consumer) throws IOException {
		int pos = filter.indexOf("/");
		if(pos == -1) {
			String regex = Globs.toUnixRegexPattern(filter);
			Pattern pattern = Pattern.compile(regex);
			if(Files.isDirectory(path)) {
				if(filter.contains("**")) {
					walkDirectory(path, child->{
						String relativePath = path.relativize(child).toString().replace("\\", "/");
						if(pattern.matcher(relativePath).matches()) {
							consumer.consume(child);
						}
					});
				} else {
					try(DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
						for(Path childPath: stream) {
							if(pattern.matcher(childPath.getFileName().toString()).matches()) {
								consumer.consume(childPath);
							}
						}
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
			Pattern segmentPattern = Pattern.compile(regex);
			if(Files.isDirectory(path)) {
				try(DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
					for(Path childPath: stream) {
						if(segmentPattern.matcher(childPath.getFileName().toString()).matches()) {
							if(segmentFilter.contains("**")) {
								Pattern fullPattern = Pattern.compile(Globs.toUnixRegexPattern(filter));
								if(Files.isDirectory(childPath)) {
									walkDirectory(childPath, p->{
										String relativePath = path.relativize(p).toString().replace("\\", "/");
										if(fullPattern.matcher(relativePath).matches()) {
											consumer.consume(p);
										}
									});
								} else {
									String relativePath = path.relativize(childPath).toString().replace("\\", "/");
									if(fullPattern.matcher(relativePath).matches()) {
										consumer.consume(childPath);
									}
								}
							} else {
								walk(filter.substring(pos + 1), childPath, consumer);
							}
						}
					}
				}
			}
		}
	}

	private static void walkDirectory(Path path, Consumer consumer) throws IOException {
		try(Stream<Path> list = Files.list(path)) {
			for(Path child: list.collect(Collectors.toList())) {
				consumer.consume(child);
				if(Files.isDirectory(child))
					walkDirectory(child, consumer);
			}
		}
	}
}
