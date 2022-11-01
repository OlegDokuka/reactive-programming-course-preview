import java.time.Duration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class MultithreadingOperatorsTask2Tests {

	@Test
	public void testSolution() {
		Thread[] threads = new Thread[1];
		StepVerifier.create(Task.subscribeOnSingleThreadScheduler(() -> {
			System.out.println("Threads:" + Thread.currentThread()
			                                      .getName());
			threads[0] = Thread.currentThread();
			return "Hello";
		}))
		            .expectSubscription()
		            .expectNext("Hello")
		            .expectComplete()
		            .verify(Duration.ofMillis(5000));

		Assertions.assertThat(threads[0])
		          .isNotEqualTo(Thread.currentThread());
	}
}