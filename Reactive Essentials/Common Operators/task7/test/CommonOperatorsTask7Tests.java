import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class CommonOperatorsTask7Tests {

	@Test
	public void testSolution() {
		Mono<Long> sequence = Task.firstFromFlux(Flux.just(1L, 2L, 3L, 4L)
		                                             .hide());

		StepVerifier.create(sequence)
		            .expectNext(1L)
		            .expectComplete()
		            .verify(Duration.ofMillis(100));
	}
}