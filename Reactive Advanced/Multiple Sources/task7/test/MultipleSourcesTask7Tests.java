import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MultipleSourcesTask7Tests {

	@Test
	public void testSolution() {
		StepVerifier.withVirtualTime(() -> Flux.from(Task.combineSeveralSources(Flux.interval(
				Duration.ofMillis(10))
		                                                                            .map(Object::toString),
				Flux.interval(Duration.ofMillis(5))
				    .map(Object::toString),
				Flux.interval(Duration.ofMillis(1))
				    .map(Object::toString)))
		                                       .take(16))
		            .expectSubscription()
		            .expectNoEvent(Duration.ofMillis(10))
		            .expectNext("008", "018", "019")
		            .expectNoEvent(Duration.ofMillis(1))
		            .expectNext("0110")
		            .thenAwait(Duration.ofMillis(4))
		            .expectNext("0111", "0112", "0113", "0213", "0214")
		            .thenAwait(Duration.ofMillis(5))
		            .expectNext("0215", "0216", "0217", "0218", "1218", "1318", "1319")
		            .verifyComplete();
	}
}