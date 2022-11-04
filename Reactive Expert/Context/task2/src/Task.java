import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class Task {

	public static Mono<String> provideCorrectContext(Mono<String> source,
			Object key,
			Object value) {
		return Mono.error(new ToDoException());
	}
}