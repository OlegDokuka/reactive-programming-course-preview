import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

public class Task {

	public static ParallelFlux<Integer> paralellizeWorkOnDifferentThreads(Flux<Integer> source) {
		return ParallelFlux.from(Flux.error(new ToDoException()));
	}
}