import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BackpressureTask3Tests {

	@Test
	public void testSolution() {
		StepVerifier.withVirtualTime(() -> Task.backpressureByBatching(
				Flux.interval(Duration.ZERO, Duration.ofMillis(10)), Duration.ofMillis(100)), 0)
		            .expectSubscription()
		            .thenRequest(1)
		            .expectNoEvent(Duration.ofMillis(100))
		            .expectNext("0123456789")
		            .thenRequest(1)
		            .expectNoEvent(Duration.ofMillis(100))
		            .expectNext("10111213141516171819")
		            .thenCancel()
		            .verify();
	}
}