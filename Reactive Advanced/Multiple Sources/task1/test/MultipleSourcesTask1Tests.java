import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MultipleSourcesTask1Tests {

	@Test
	public void mergeSeveralSourcesTest() {
		StepVerifier
				.withVirtualTime(() -> Task.mergeSeveralSources(
						Flux.just("A").delaySubscription(Duration.ofSeconds(1)),
						Flux.just("B")
				))
				.expectSubscription()
				.expectNext("B")
				.expectNoEvent(Duration.ofSeconds(1))
				.expectNext("A")
				.verifyComplete();
	}
}