import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MultithreadingOperatorsTask5Tests {

	@Test
	public void testSolution() {
		Thread[] threads = new Thread[3];
		StepVerifier.create(Task.paralellizeLongRunningWorkOnUnboundedAmountOfThread(Flux.<Callable<String>>just(
				() -> {
					threads[0] = Thread.currentThread();
					Thread.sleep(300);
					return "Hello";
				},
				() -> {
					threads[1] = Thread.currentThread();
					Thread.sleep(300);
					return "Hello";
				},
				() -> {
					threads[2] = Thread.currentThread();
					Thread.sleep(300);
					return "Hello";
				}).repeat(20)))
		            .expectSubscription()
		            .expectNext("Hello", "Hello", "Hello")
		            .expectNextCount(60)
		            .expectComplete()
		            .verify(Duration.ofMillis(600));

		Assertions.assertThat(threads[0])
		          .as("Expected execution on different Threads")
		          .isNotNull()
		          .isNotEqualTo(threads[1]);
		Assertions.assertThat(threads[1])
		          .as("Expected execution on different Threads")
		          .isNotEqualTo(threads[2]);
		Assertions.assertThat(threads[0])
		          .as("Expected execution on different Threads")
		          .isNotEqualTo(threads[2]);
	}
	@Test
	public void testSolution1() {
		ConcurrentHashMap<Thread, Object> threads = new ConcurrentHashMap<>();
		StepVerifier.create(Task.paralellizeLongRunningWorkOnUnboundedAmountOfThread(Flux.<Callable<String>>just(
				() -> {
					threads.put(Thread.currentThread(), 1);
					Thread.sleep(500);
					return "Hello";
				}).repeat(256)))
		            .expectSubscription()
		            .expectNext("Hello", "Hello", "Hello")
		            .expectNextCount(254)
		            .expectComplete()
		            .verify(Duration.ofMillis(10000));

		Assertions.assertThat(threads)
		          .as("Expected execution on different Threads")
		          .isNotNull()
		          .hasSizeLessThanOrEqualTo(257);
	}
}