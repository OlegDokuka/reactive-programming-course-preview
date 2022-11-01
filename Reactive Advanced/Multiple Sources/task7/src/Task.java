import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public class Task {

	public static Publisher<String> combineSeveralSources(Publisher<String> prefixPublisher,
			Publisher<String> wordPublisher,
			Publisher<String> suffixPublisher) {

		return Flux.error(new ToDoException());
	}
}