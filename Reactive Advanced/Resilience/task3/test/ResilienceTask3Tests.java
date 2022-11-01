import java.util.concurrent.Callable;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ResilienceTask3Tests {

	@Test
	public void testSolution() throws Exception {
		Callable<String> callable = Mockito.mock(Callable.class);
		Mockito.when(callable.call())
		       .thenThrow(new RuntimeException())
		       .thenReturn("Hello");

		StepVerifier
				.create(Task.retryOnError(Mono.fromCallable(callable)))
				.expectSubscription()
				.expectNext("Hello")
				.expectComplete()
				.verify();
	}
}