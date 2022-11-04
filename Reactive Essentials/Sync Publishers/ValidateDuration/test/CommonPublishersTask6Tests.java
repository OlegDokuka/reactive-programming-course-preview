import java.time.Duration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class CommonPublishersTask6Tests {

	@Test
	public void testValid() {
		StepVerifier.withVirtualTime(() -> {
						Object object = ValidationTask.validate(Duration.ofMillis(10));
						if (object instanceof Mono) {
							return ((Mono) object);
						}

						Assertions.fail("fix implementation and API");
						return Mono.empty();
					})
		            .expectSubscription()
					.expectComplete()
		            .verify(Duration.ofMillis(100));
	}

	@Test
	public void testInvalid() {
		StepVerifier.withVirtualTime(() -> {
					Object object = ValidationTask.validate(Duration.ofMillis(-10));
					if (object instanceof Mono) {
						return ((Mono) object);
					}

					Assertions.fail("fix implementation and API");
					return Mono.empty();
				})
				.expectSubscription()
				.expectError()
				.verify(Duration.ofMillis(100));
	}
}