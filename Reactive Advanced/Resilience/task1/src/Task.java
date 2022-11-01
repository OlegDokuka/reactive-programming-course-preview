import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public class Task {

	public static Publisher<String> fallbackIfEmpty(Flux<String> publisher, String fallback) {
		return Flux.error(new ToDoException());
	}
}