import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Paths;
import java.util.stream.Stream;

public class NonBlockingTextFileStreamTask {

	static Files files;

	public static Flux<String> readFile(String filename) {
		return Flux.using(() -> files.lines(Paths.get(filename)), Flux::fromStream, Stream::close);
	}
}