import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Task {

	public static Publisher<String> publishOnParallelThreadScheduler(Flux<String> source) {
		return Flux.error(new ToDoException());
	}
}