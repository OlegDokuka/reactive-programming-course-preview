import java.time.Duration;
import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

public class DLMTask2Tests {

	@Test
	public void testSolution() {
		ArrayList<Publisher<?>> publishers = new ArrayList<>();
		Hooks.onEachOperator(p -> {
			publishers.add(p);
			return p;
		});

		try {
			StepVerifier.create(Flux.interval(Duration.ofMillis(10))
			                        .transform(Task::loggerTask))
			            .expectSubscription()
			            .expectNextCount(10)
			            .thenCancel()
			            .verify();

			Assertions.assertThat(publishers)
			          .anyMatch(p -> {
			          	return p.getClass().getSimpleName().startsWith("FluxLog");
			          });
		} finally {
			Hooks.resetOnEachOperator();
		}
	}
}