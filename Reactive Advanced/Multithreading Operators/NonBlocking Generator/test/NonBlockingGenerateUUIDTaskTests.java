import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.blockhound.BlockHound;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;

public class NonBlockingGenerateUUIDTaskTests {

	static {
		BlockHound.install();
	}

	@Test
	public void testSolution() {
		Thread[] threads = new Thread[1];
		Thread[] originThreads = new Thread[1];
		UUIDGenerator mock = Mockito.mock(UUIDGenerator.class);
		Mockito.when(mock.secureUUID()).thenAnswer(a -> {
			threads[0] = Thread.currentThread();
			Thread.yield();
			return "Hello";
		});
		NonBlockingGenerateUUIDTask.uuidGenerator = mock;
		StepVerifier.create(NonBlockingGenerateUUIDTask.generateRandomUUID()
						.doFirst(() -> originThreads[0] = Thread.currentThread())
						.subscribeOn(Schedulers.parallel()))
		            .expectSubscription()
		            .expectNext("Hello")
		            .expectComplete()
		            .verify(Duration.ofMillis(5000));

		Assertions.assertThat(threads[0])
		          .isNotEqualTo(originThreads[0]);
	}
}