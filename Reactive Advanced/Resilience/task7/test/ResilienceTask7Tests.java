import java.util.function.Function;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ResilienceTask7Tests {

	@Test
	public void testSolution() {
		Function<Integer, Integer> mapping = i -> 10 / i;
		Flux<Integer> range = Flux.range(0, 6);

		StepVerifier.create(Task.provideHandmadeContinuation(range, mapping))
		            .expectSubscription()
		            .expectNextCount(5)
		            .expectAccessibleContext()
		            .hasSize(0)
		            .then()
		            .expectComplete()
		            .verify();
	}
}