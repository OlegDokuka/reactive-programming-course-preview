import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MultipleSourcesTask6Tests {

	@Test
	public void testSolution() {
		StepVerifier.withVirtualTime(() -> Flux.from(Task.zipSeveralSources(Flux.interval(
				Duration.ofMillis(10))
		                                                                        .map(Object::toString),
				Flux.interval(Duration.ofMillis(5))
				    .map(Object::toString),
				Flux.interval(Duration.ofMillis(1))
				    .map(Object::toString)))
		                                       .take(3))
		            .expectSubscription()
		            .expectNoEvent(Duration.ofMillis(10))
		            .expectNext("000")
		            .expectNoEvent(Duration.ofMillis(10))
		            .expectNext("111")
		            .expectNoEvent(Duration.ofMillis(10))
		            .expectNext("222")
		            .verifyComplete();
	}
}