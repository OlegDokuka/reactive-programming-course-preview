import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class CommonOperatorsTask2Tests {

	@Test
	public void testSolution() {
		Flux<String> sequence =
				Task.transformSequence(Flux.just("1", "10", "100", "1000", "10000"));

		StepVerifier.create(sequence)
		            .expectNext("1000", "10000")
		            .expectComplete()
		            .verify(Duration.ofMillis(100));
	}
}