import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

public class MultipleSourcesTask2Tests {

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

					return Task.mergeSeveralSourcesSequential(
							probeA.mono(),
							probeB.mono()
					);
				}, 0)
				.expectSubscription()
				.then(() -> probes[0].assertWasSubscribed())
				.then(() -> probes[1].assertWasSubscribed())
				.thenRequest(2)
				.expectNoEvent(Duration.ofSeconds(1))
				.expectNext("VANILLA")
				.expectNext("CHOCOLATE")
				.verifyComplete();
	}
}