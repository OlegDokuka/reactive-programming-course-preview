import java.time.Duration;
import java.util.ArrayList;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

public class DLMTask3Tests {

	@Test
	public void testSolution() {
		SimpleMeterRegistry registry = new SimpleMeterRegistry();
		Metrics.globalRegistry.add(registry);
		ArrayList<Publisher<?>> publishers = new ArrayList<>();
		Hooks.onEachOperator(p -> {
			publishers.add(p);
			return p;
		});

		try {
			StepVerifier.create(Flux.interval(Duration.ofMillis(10))
			                        .transform(Task::metricsTask))
			            .expectSubscription()
			            .expectNextCount(10)
			            .thenCancel()
			            .verify();

			Assertions.assertThat(publishers)
			          .anyMatch(p -> {
			          	return p.getClass().getSimpleName().startsWith("FluxMetrics");
			          });

			Assertions.assertThat(registry.getMeters())
			          .isNotEmpty();
		} finally {
			Hooks.resetOnEachOperator();
		}
	}
}