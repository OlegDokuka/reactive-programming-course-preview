import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ResilienceTask8Tests {

	@Test
	public void testSolution() {
		StepVerifier
				.create(Task.retryWithBackoffOnError(Flux.error(new RuntimeException())))
				.expectSubscription()
				.expectErrorMatches(e -> e.getClass().equals(RuntimeException.class))
				.verify(Duration.ofMillis(50));
	}

	@Test
	public void testSolution1() {
		StepVerifier
				.create(Task.retryWithBackoffOnError(Flux.just("123")))
				.expectSubscription()
				.expectNext("123")
				.verifyComplete();
	}


	@Test
	public void testSolution2() {
		Queue<Flux<String>> fluxesQueue = new LinkedList<>(Arrays.asList(
			Flux.error(new RuntimeException("[Retry] Error 0")),
			Flux.error(new RuntimeException("[Retry] Error 1")),
			Flux.error(new RuntimeException("[Retry] Error 2")),
			Flux.error(new RuntimeException("[Retry] Error 3")),
			Flux.error(new RuntimeException("[Nope ] Error 4")),
			Flux.just("Should not happen")
		));
		StepVerifier
				.withVirtualTime(() -> Task.retryWithBackoffOnError(Flux.defer(fluxesQueue::poll)))
				.expectSubscription()
			    .expectNoEvent(Duration.ofMillis(1000))
				.thenAwait(Duration.ofMillis(2000))
				.expectErrorMessage("[Nope ] Error 4")
				.verify();
	}



	@Test
	public void testSolution3() {
		Queue<Flux<String>> fluxesQueue = new LinkedList<>(Arrays.asList(
				Flux.error(new RuntimeException("[Retry] Error 0")),
				Flux.error(new RuntimeException("[Retry] Error 1")),
				Flux.error(new RuntimeException("[Retry] Error 2")),
				Flux.error(new RuntimeException("[Retry] Error 3")),
				Flux.error(new RuntimeException("[Retry] Error 4")),
				Flux.error(new RuntimeException("[Retry] Error 5"))
		));
		StepVerifier
				.withVirtualTime(() -> Task.retryWithBackoffOnError(Flux.defer(fluxesQueue::poll)))
				.expectSubscription()
				.expectNoEvent(Duration.ofMillis(2000))
				.thenAwait(Duration.ofMillis(2000))
				.consumeErrorWith(e ->
					Assertions.assertThat(e)
					          .hasCause(new RuntimeException("[Retry] Error 5"))
					          .matches(Exceptions::isRetryExhausted)
				)
				.verify();
	}
}