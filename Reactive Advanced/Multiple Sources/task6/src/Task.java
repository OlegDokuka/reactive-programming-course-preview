import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.function.TupleUtils;

public class Task {

	public static Publisher<String> zipSeveralSources(Publisher<String> prefixPublisher,
			Publisher<String> wordPublisher,
			Publisher<String> suffixPublisher) {
		return Flux.error(new ToDoException());
	}
}