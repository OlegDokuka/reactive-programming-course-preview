import java.util.function.BiConsumer;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public class Task {

	public static Publisher<Integer> provideSupportOfContinuation(Flux<Integer> values,
			BiConsumer<Throwable, Object> consumer) {
		return Flux.error(new ToDoException());
	}
}