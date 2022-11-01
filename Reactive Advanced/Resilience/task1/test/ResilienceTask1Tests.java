import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ResilienceTask1Tests {

	@Test
	public void test1() {
		StepVerifier
				.create(Task.fallbackIfEmpty(Flux.empty(), "Hello"))
				.expectSubscription()
				.expectNext("Hello")
				.verifyComplete();
	}


	@Test
	public void test2() {
		StepVerifier
				.create(Task.fallbackIfEmpty(Flux.just("abc"), "Hello"))
				.expectSubscription()
				.expectNext("abc")
				.verifyComplete();
	}
}