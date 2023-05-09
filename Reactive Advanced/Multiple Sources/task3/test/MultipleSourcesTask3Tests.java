import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

public class MultipleSourcesTask3Tests {

	@Test
	public void testSolution() {
		PublisherProbe[] probes = new PublisherProbe[2];

		StepVerifier
				.withVirtualTime(() -> {
					PublisherProbe<String> probeA = PublisherProbe.of(Mono.fromCallable(() -> "VANILLA").delaySubscription(
							Duration.ofSeconds(1)));
					PublisherProbe<String> probeB = PublisherProbe.of(Mono.fromCallable(() -> "CHOCOLATE"));

					probes[0] = probeA;
					probes[1] = probeB;

					return Task.concatSeveralSourcesOrdered(
							probeA.mono(),
							probeB.mono()
					);
				}, 0)
				.expectSubscription()
				.then(() -> probes[0].assertWasSubscribed())
				.then(() -> probes[1].assertWasNotSubscribed())
				.thenRequest(1)
				.then(() -> probes[0].assertWasRequested())
				.then(() -> probes[1].assertWasNotSubscribed())
				.expectNoEvent(Duration.ofSeconds(1))
				.expectNext("VANILLA")
				.thenRequest(1)
				.then(() -> probes[1].assertWasSubscribed())
				.then(() -> probes[1].assertWasRequested())
				.expectNext("CHOCOLATE")
				.verifyComplete();
	}
}