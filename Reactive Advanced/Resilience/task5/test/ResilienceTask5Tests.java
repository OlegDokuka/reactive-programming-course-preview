import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class ResilienceTask5Tests {
	static {
		System.setProperty("reactor.schedulers.defaultPoolSize", "1");
	}

	@Test
	public void testSolution() {
		StepVerifier
			.create(
				Task
					.timeoutBlockingOperation(() -> {
						try {
							Thread.sleep(1000000);
						}
						catch (InterruptedException e) {
							return null;
						}

						return "Toooooo long";
					}, Duration.ofSeconds(1), "Hello")
					.subscribeOn(Schedulers.parallel())
			)
            .expectSubscription()
            .expectNext("Hello")
            .expectComplete()
            .verify(Duration.ofSeconds(2));
	}
}