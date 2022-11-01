import reactor.core.publisher.Flux;

public class Task {

	public static Flux<String> transformSequence(Flux<String> stringFlux) {
		return Flux.error(new ToDoException());
	}
}