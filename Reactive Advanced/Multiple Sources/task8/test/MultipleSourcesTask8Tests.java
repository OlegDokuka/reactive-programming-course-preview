import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MultipleSourcesTask8Tests {

	@Test
	public void testSolution() {
		StepVerifier
				.withVirtualTime(() -> Task.fillIceCreamWaffleBowl(
						Flux.just(Task.IceCreamType.VANILLA, Task.IceCreamType.VANILLA, Task.IceCreamType.CHOCOLATE, Task.IceCreamType.VANILLA, Task.IceCreamType.CHOCOLATE, Task.IceCreamType.CHOCOLATE).delayElements(
								Duration.ofSeconds(1)),
						Flux.interval(Duration.ofMillis(500)).map(i -> Task.IceCreamBall.ball("A" + i)).onBackpressureDrop().publish(1).autoConnect(0),
						Flux.interval(Duration.ofMillis(200)).map(i -> Task.IceCreamBall.ball("B" + i)).onBackpressureDrop().publish(1).autoConnect(0)
				).map(Object::toString))
				.expectSubscription()
				.expectNoEvent(Duration.ofSeconds(1))
				.thenAwait(Duration.ofSeconds(1))
				.expectNext("A1", "A2")
				.thenAwait(Duration.ofSeconds(1))
				.expectNext("A3", "A4")
				.thenAwait(Duration.ofSeconds(1))
				.expectNext("B14", "B15", "B16", "B17", "B18")
				.thenAwait(Duration.ofSeconds(1))
				.expectNext("A5", "A7", "A8")
				.thenAwait(Duration.ofSeconds(1))
				.expectNext("B19", "B24", "B25", "B26", "B27", "B28")
				.thenAwait(Duration.ofSeconds(1))
				.expectNext("B29", "B30", "B31", "B32", "B33")
				// Actually the last source is infinitive stream
				// and because of switchMap nature we required to cancel the upstream
				// instead of merely awaiting the completion signal from upstream
				.thenCancel()
				.verify();
	}
}