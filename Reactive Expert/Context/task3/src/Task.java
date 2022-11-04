import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

public class Task {

	public static Flux<String> provideCorrectContext(Publisher<String> sourceA,
			Context contextA,
			Publisher<String> sourceB,
			Context contextB) {
		return Flux.from(sourceA).mergeWith(sourceB);
	}
}