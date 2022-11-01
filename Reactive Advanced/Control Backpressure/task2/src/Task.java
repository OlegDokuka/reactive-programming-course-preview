import java.time.Duration;
import java.util.List;

import reactor.core.publisher.Flux;

public class Task {

	public static Flux<List<Long>> backpressureByBatching(Flux<Long> upstream,
			Duration duration) {
		return Flux.error(new ToDoException());
	}
}