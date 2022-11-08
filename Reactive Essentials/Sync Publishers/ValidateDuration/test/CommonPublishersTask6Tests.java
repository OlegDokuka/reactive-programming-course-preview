import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class CommonPublishersTask6Tests {

	@Test
	public void testValid() {
		StepVerifier.withVirtualTime(() -> ValidationTask.validate(Duration.ofMillis(10)))
		            .expectSubscription()
					.expectComplete()
		            .verify(Duration.ofMillis(100));
	}

	@Test
	public void testInvalid() {
		StepVerifier.withVirtualTime(() -> ValidationTask.validate(Duration.ofMillis(-10)))
				.expectSubscription()
				.expectError()
				.verify(Duration.ofMillis(100));
	}
}