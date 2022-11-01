import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class DLMTask1Tests {

	@Test
	public void testSolution() {
		StepVerifier.create(Flux.interval(Duration.ofMillis(10))
		                        .publishOn(Schedulers.single())
		                        .transform(Task::checkAndDebug)
		                        .take(10)
		                        .subscribeOn(Schedulers.single()))
		            .expectSubscription()
		            .expectNextCount(10)
		            .verifyComplete();
	}
}