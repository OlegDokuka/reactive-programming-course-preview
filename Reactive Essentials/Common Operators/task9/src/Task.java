import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Task {

	public static Mono<Void> waitUntilFluxCompletion(Flux<Long> flux) {
		return Mono.error(new ToDoException());
	}
}