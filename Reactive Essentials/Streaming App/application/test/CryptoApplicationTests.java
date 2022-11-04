import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

public class CryptoApplicationTests {

	@Test
	public void verifyIncomingMessageValidation() {
		StepVerifier.create(
				ApplicationRunner.handleRequestedAveragePriceIntervalValue(
						Flux.just("Invalid", "32", "", "-1", "1", "0", "5", "62", "5.6", "12")
				).take(Duration.ofSeconds(1)))
		            .expectNext(32L, 1L, 5L, 12L)
		            .verifyComplete();
	}
}