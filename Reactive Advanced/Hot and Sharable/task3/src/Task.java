import org.reactivestreams.Publisher;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

public class Task {

	public static Publisher<String> transformToHotUsingProcessor(Flux<String> coldSource) {
		return Flux.error(new ToDoException());
	}
}