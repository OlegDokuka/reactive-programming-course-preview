import java.time.Duration;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Task {

	public static Publisher<String> timeoutOnce(Flux<String> flux,
			Duration duration, String fallback) {
		return Mono.error(new ToDoException());
	}
}