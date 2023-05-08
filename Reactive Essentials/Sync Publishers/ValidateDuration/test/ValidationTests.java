import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ValidationTests {

	@Test
	public void testValid() {
		StepVerifier.withVirtualTime(() -> {
					Object validate = ValidationTask.validate(Duration.ofMillis(10));

					if (!(validate instanceof Mono)) {
						return Mono.error(new IllegalStateException("Expected Mono return type"));
					}

					return ((Mono<Void>) validate);
				})
		            .expectSubscription()
					.expectComplete()
		            .verify(Duration.ofMillis(100));
	}

	@Test
	public void testInvalid() {
		StepVerifier.withVirtualTime(() -> {
					Object validate = ValidationTask.validate(Duration.ofMillis(-10));
					if (!(validate instanceof Mono)) {
						return Mono.error(new IllegalStateException("Expected Mono return type"));
					}
					return ((Mono<Void>) validate);
				})
				.expectSubscription()
				.expectError()
				.verify(Duration.ofMillis(100));
	}
}