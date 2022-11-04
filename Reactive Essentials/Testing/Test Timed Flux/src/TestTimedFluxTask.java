import java.time.Duration;
import java.util.function.Supplier;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class TestTimedFluxTask {

	public static void verifyEmissionWithVirtualTimeScheduler() {
		Supplier<Flux<Long>> toVerify = () -> Flux.interval(Duration.ofDays(1))
		                                          .take(15)
		                                          .skip(5);
		throw new ToDoException();
	}
}