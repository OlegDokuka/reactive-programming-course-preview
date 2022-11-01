import java.time.Duration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class CommonOperatorsTask10Tests {

	@Test
	public void testSolution() {
		Long value = Task.transformToImperative(Flux.interval(Duration.ofMillis(5))
		                                            .take(10));

		Assertions.assertThat(value).isEqualTo(9);
	}
}