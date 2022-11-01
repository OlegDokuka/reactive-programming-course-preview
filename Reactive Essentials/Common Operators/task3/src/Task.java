import reactor.core.publisher.Flux;

public class Task {

	public static Flux<Character> createSequence(Flux<String> stringFlux) {
		return Flux.error(new ToDoException());
	}
}