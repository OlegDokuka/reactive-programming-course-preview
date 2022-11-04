import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

public class SchedulePeriodicTaskTests {

	@Test
	@Timeout(value = 5, unit = TimeUnit.SECONDS)
	public void testSolution() {
		VirtualTimeScheduler virtualTimeScheduler = VirtualTimeScheduler.getOrSet();
		try {
			AtomicInteger called = new AtomicInteger();

			Object result = Assertions.assertTimeout(Duration.ofSeconds(1), () -> SchedulePeriodicTask.executeTaskPeriodically(() -> called.incrementAndGet(), 1000));

			if (result instanceof Flux) {
				StepVerifier.withVirtualTime(() -> ((Flux<Long>) result))
						.expectSubscription()
						.expectNoEvent(Duration.ofSeconds(1))
						.expectNextCount(1)
						.thenAwait(Duration.ofSeconds(3))
						.expectNextCount(3)
						.thenCancel()
						.verify();
			}

			org.assertj.core.api.Assertions.assertThat(called.get()).isEqualTo(4);
		} finally {
			VirtualTimeScheduler.reset();
		}
	}
}