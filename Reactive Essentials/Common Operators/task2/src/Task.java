import reactor.core.publisher.Flux;

public class Task {

	public static Flux<String> transformSequence(Flux<String> flux) {
		return Flux.error(new ToDoException());
	}
}