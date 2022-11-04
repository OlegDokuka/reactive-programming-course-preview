import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

public class DelayExecutionTaskTests {

	@Test
	public void testResultIsCorrect() {
		VirtualTimeScheduler virtualTimeScheduler = VirtualTimeScheduler.getOrSet();
		try {

			Object sequence = DelayExecutionTask.pauseExecution(1000);

			if (sequence instanceof Mono) {
				StepVerifier.withVirtualTime(() -> ((Mono<Long>) sequence))
						.expectSubscription()
						.expectNoEvent(Duration.ofSeconds(1))
						.expectNextCount(1)
						.expectComplete()
						.verify(Duration.ofMillis(100));
			} else {
				Assertions.fail("Unexpected implementation");
			}
		} finally {
			VirtualTimeScheduler.reset();
		}
	}
}