import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.test.StepVerifier;

import java.time.Duration;

public class NonBlockingGenerateUUIDTaskTests {

	@Test
	public void testSolution() {
		Thread[] threads = new Thread[1];
		UUIDGenerator mock = Mockito.mock(UUIDGenerator.class);
		Mockito.when(mock.secureUUID()).thenAnswer(a -> {
			threads[0] = Thread.currentThread();
			return "Hello";
		});
		NonBlockingGenerateUUIDTask.uuidGenerator = mock;
		StepVerifier.create(NonBlockingGenerateUUIDTask.generateRandomUUID())
		            .expectSubscription()
		            .expectNext("Hello")
		            .expectComplete()
		            .verify(Duration.ofMillis(5000));

		Assertions.assertThat(threads[0])
		          .isNotEqualTo(Thread.currentThread());
	}
}