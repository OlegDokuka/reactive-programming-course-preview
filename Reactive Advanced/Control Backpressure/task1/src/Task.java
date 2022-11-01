import reactor.core.publisher.Flux;

public class Task {

	public static Flux<RefCounted> dropElementsOnBackpressure(Flux<RefCounted> upstream) {
		return Flux.error(new ToDoException());
	}
}