import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ResilienceTask9Tests {

	@Test
	public void testSolution() {
		StepVerifier
			.withVirtualTime(() -> Task.timeoutOnce(
				Flux.interval(Duration.ofDays(1))
				    .mergeWith(Mono.delay(Duration.ofMillis(900)).thenReturn(-1L))
				    .map(Object::toString),
				Duration.ofMillis(1000),
				"Hello"
			))
			.expectSubscription()
			.expectNoEvent(Duration.ofMillis(900))
			.expectNext("-1")
			.expectNoEvent(Duration.ofDays(1).minus(Duration.ofMillis(900)))
			.expectNext("0")
			.expectNoEvent(Duration.ofDays(1))
			.expectNext("1")
			.thenCancel()
			.verify(Duration.ofSeconds(1));
	}

	@Test
	public void testSolution1() {
		StepVerifier
				.withVirtualTime(() -> Task.timeoutOnce(
						Flux.interval(Duration.ofDays(1))
						    .map(Object::toString),
						Duration.ofMillis(1000),
						"Hello"
				))
				.expectSubscription()
				.expectNoEvent(Duration.ofSeconds(1))
				.expectNext("Hello")
				.expectComplete()
				.verify(Duration.ofSeconds(1));
	}
}