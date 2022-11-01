import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class CommonOperatorsTask6Tests {

	@Test
	public void testSolution() {
		Mono<Long> sequence = Task.createSequence(Flux.range(0, 5)
		                                              .map(Integer::longValue));

		StepVerifier.create(sequence)
		            .expectNext(10L)
		            .expectComplete()
		            .verify(Duration.ofMillis(100));
	}
}