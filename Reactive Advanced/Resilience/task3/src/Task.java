import java.time.Duration;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class Task {

	public static Publisher<String> retryOnError(Mono<String> publisher) {
		return Mono.error(new ToDoException());
	}
}