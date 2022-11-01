import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public class Task {

	public static Publisher<String> mergeSeveralSources(Publisher<String>... sources) {
		return Flux.error(new ToDoException());
	}
}