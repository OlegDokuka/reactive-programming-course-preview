import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class ResilienceTask4Tests {

	@Test
	public void testSolution() {
		StepVerifier.withVirtualTime(() -> Task.timeoutLongOperation(CompletableFuture.supplyAsync(
				() -> {
					try {
						Thread.sleep(1000000);
					}
					catch (InterruptedException e) {
						return null;
					}

					return "Toooooo long";
				}), Duration.ofMillis(1000), "Hello"))
		            .expectSubscription()
		            .expectNoEvent(Duration.ofSeconds(1))
		            .expectNext("Hello")
		            .expectComplete()
		            .verify(Duration.ofSeconds(1));
	}
}