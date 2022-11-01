import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;

public class TextFileStreamTask {

	public static Iterable<String> readFile(String filename) {
		try {
			return Files.lines(Paths.get(filename)).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}