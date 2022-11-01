import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MultithreadingOperatorsTask1Tests {

	@Test
	public void mergeSeveralSourcesTest() {
		Thread[] threads = new Thread[2];
		StepVerifier
				.create(Flux.from(Task.publishOnParallelThreadScheduler(Flux.defer(() -> {
					threads[0] = Thread.currentThread();
					return Flux.just("Hello");
				}))).doFinally(__ -> threads[1] = Thread.currentThread()))
				.expectSubscription()
				.expectNext("Hello")
				.verifyComplete();

		Assertions.assertThat(threads[0])
		          .isNotNull()
		          .isNotEqualTo(threads[1]);
		Assertions.assertThat(threads[1])
		          .isNotNull();
	}
}