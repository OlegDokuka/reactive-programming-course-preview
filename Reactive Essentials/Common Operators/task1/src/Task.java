import java.util.Objects;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public class Task {

	public static <T> Publisher<String> transformSequence(Publisher<T> publisher) {
		return Flux.error(new ToDoException());
	}
}