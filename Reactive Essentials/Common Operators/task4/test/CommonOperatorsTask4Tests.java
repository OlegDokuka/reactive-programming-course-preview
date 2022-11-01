import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class CommonOperatorsTask4Tests {

	@Test
	public void testSolution() {
		Flux<String> sequence =
				Task.transformSequence(Flux.just("ABC", "dEf", "gfh", "j"));

		StepVerifier.create(sequence)
		            .expectNext("gfh", "j")
		            .expectComplete()
		            .verify(Duration.ofMillis(100));
	}
}