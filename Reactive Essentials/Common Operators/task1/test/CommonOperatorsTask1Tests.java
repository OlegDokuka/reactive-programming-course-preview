import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class CommonOperatorsTask1Tests {

	@Test
	public void testResultIsCorrect() {
		Publisher<String> sequence =
				Task.transformSequence(Flux.fromArray(new Object[]{new Object() {
					@Override
					public String toString() {
						return "test";
					}
				}, 1, "test2"}));

		StepVerifier.create(sequence)
		            .expectNext("test", "1", "test2")
		            .expectComplete()
		            .verify(Duration.ofMillis(100));
	}
}