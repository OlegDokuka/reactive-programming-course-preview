import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FlattenWordsTests {

	@Test
	public void testSolution() {
		Object sequence = FlattenWordsTask.flattenWordsIntoLetters(Flux.just("ABC", "dEf", "gfh", "j"));

		if (!(sequence instanceof Flux)) {
			Assertions.fail("Unexpected sequence type");
		}

		StepVerifier.create(((Flux<Character>) sequence))
		            .expectNext('A', 'B', 'C', 'd', 'E', 'f', 'g', 'f', 'h', 'j')
		            .expectComplete()
		            .verify(Duration.ofMillis(100));
	}
}