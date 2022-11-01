import java.time.Duration;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

public class Task {

	static final int RETRY_COUNT = 5;
	static final String IF_MESSAGE_STARTS_WITH = "[Retry]";


	public static Publisher<String> retryWithBackoffOnError(Flux<String> publisher) {
		return Flux.error(new ToDoException());
	}
}