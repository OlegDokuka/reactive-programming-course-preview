import java.util.concurrent.Callable;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Task {

	public static Publisher<String> subscribeOnSingleThreadScheduler(Callable<String> blockingCall) {
		return Mono.error(new ToDoException());
	}
}