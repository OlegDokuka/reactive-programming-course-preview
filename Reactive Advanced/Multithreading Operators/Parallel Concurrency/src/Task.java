import java.util.concurrent.Callable;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class Task {

	public static Publisher<String> parallelizeLongRunningWorkOnUnboundedAmountOfThread(
			Flux<Callable<String>> streamOfLongRunningSources) {
		return Flux.error(new ToDoException());
	}
}