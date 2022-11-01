import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public class Task {

	public static Publisher<String> readFile(String filename) {
		return Flux.error(new ToDoException());
	}
}