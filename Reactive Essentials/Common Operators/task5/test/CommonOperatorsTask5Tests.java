import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class CommonOperatorsTask5Tests {

	@Test
	public void testSolution() {
		Flux<Long> sequence = Task.transformSequence(Flux.just("0xFF",
				"00FF",
				"asdas",
				"0xFFFFFF",
				"0x00000000"));

		StepVerifier.create(sequence)
		            .expectNext(Long.parseLong("FF", 16),
				            Long.parseLong("FFFFFF", 16),
				            Long.parseLong("00000000", 16))
		            .expectComplete()
		            .verify(Duration.ofMillis(100));
	}
}