import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class AsyncHttpGetRequestTaskTests {

	@Test
	public void testSolution() {
		AsyncRestTemplate mock = Mockito.mock(AsyncRestTemplate.class);
		Mockito.when(mock.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(CompletableFuture.supplyAsync(() -> "Lorem ipsum dolor"));
		FutureHttpGetRequestTask.asyncRestTemplate = mock;
		Object sequence = FutureHttpGetRequestTask.getLorem();

		if (sequence instanceof Mono) {

			StepVerifier.create(((Mono<String>) sequence))
					.expectNext("Lorem ipsum dolor")
					.expectComplete()
					.verify();
		} else {
			Assertions.fail("Refactor given code using Mono api");
		}
	}
}