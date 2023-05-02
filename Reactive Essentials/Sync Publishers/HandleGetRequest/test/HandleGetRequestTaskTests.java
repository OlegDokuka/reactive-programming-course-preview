import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class HandleGetRequestTaskTests {

	@Test
	public void testSolution1() {
		ReactiveRandom reactiveRandom = Mockito.mock(ReactiveRandom.class);
		for (int i = 0; i < 100; i++) {
			int expectedNumber = ThreadLocalRandom.current().nextInt();
			Mockito.when(reactiveRandom.nextInt()).thenReturn(Mono.just(expectedNumber));
			HandleGetRequestTask.reactiveRandom = reactiveRandom;
			Object outcome = HandleGetRequestTask.getRandomNumberHandler(new RequestEntity<Void>(HttpMethod.GET, null));

			if (!(outcome instanceof Mono)) {
				Assertions.fail("Expected Mono as a return type");
				return;
			}

			StepVerifier.create(((Mono<Integer>) outcome))
					.expectNext(expectedNumber)
					.expectComplete()
					.verify(Duration.ofMillis(100));
		}
	}

	@Test
	public void testSolution2() {
		for (HttpMethod method : HttpMethod.values()) {
			if (method == HttpMethod.GET) {
				continue;
			}

			Publisher<Integer> sequence = HandleGetRequestTask.getRandomNumberHandler(new RequestEntity<Void>(method, null));

			StepVerifier.create(sequence)
					.expectError()
					.verify(Duration.ofMillis(100));
		}
	}
}