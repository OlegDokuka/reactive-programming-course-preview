import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public class Task {

	public static Publisher<String> mergeSeveralSourcesSequential(Publisher<String>... sources) {
		return Flux.merge(sources);
	}
}