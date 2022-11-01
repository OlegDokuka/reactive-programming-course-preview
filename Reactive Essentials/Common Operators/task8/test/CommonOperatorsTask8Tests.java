import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class CommonOperatorsTask8Tests {

	@Test
	public void testSolution() {
		Mono<List<String>> sequence = Task.transformToList(Flux.range(0, 10)
		                                                       .map(Objects::toString));

		StepVerifier.create(sequence)
		            .expectNext(Arrays.asList("0",
				            "1",
				            "2",
				            "3",
				            "4",
				            "5",
				            "6",
				            "7",
				            "8",
				            "9"))
		            .expectComplete()
		            .verify(Duration.ofMillis(100));
	}
}