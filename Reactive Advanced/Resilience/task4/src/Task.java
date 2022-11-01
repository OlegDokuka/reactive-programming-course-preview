import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class Task {

	public static Publisher<String> timeoutLongOperation(CompletableFuture<String> longRunningCall,
			Duration duration, String fallback) {
		return Mono.error(new ToDoException());
	}
}