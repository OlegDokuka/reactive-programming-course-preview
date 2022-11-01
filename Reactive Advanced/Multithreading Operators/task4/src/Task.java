import java.util.function.Function;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Task {


	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Flux<Long> modifyStreamExecution(
			Flux<Long> flux,
			Function work1, Function work2) {
		return flux
				.map(work1)
				// fixme
				// fixme
				.map(work2);
	}
}