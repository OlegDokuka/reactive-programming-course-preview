import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.UnicastProcessor;
import reactor.test.StepVerifier;

public class HnSTask4Tests {

	@Test
	public void testSolution2() {
		ArrayList<Publisher> operators = new ArrayList<>();
		Hooks.onEachOperator(e -> {
			operators.add(e);
			return e;
		});

		try {
			UnicastProcessor<String> source = UnicastProcessor.create();
			ReplayProcessor<String> consumer1 = ReplayProcessor.create(10);
			ReplayProcessor<String> consumer2 = ReplayProcessor.create(10);

			Publisher<String> publisher = Task.transformToHot2(source);

			publisher.subscribe(consumer1);

			source.onNext("A");
			source.onNext("B");
			source.onNext("C");

			publisher.subscribe(consumer2);

			source.onNext("D");
			source.onNext("E");
			source.onNext("F");

			source.onComplete();

			StepVerifier.create(consumer1)
			            .expectSubscription()
			            .expectNext("A", "B", "C", "D", "E", "F")
			            .verifyComplete();

			StepVerifier.create(consumer2)
			            .expectSubscription()
			            .expectNext("D", "E", "F")
			            .verifyComplete();
		}
		finally {
			Hooks.resetOnEachOperator();
		}

		Assertions.assertThat(operators)
		          .as("Expected usage of built-in operator")
		          .noneMatch(p -> !p.getClass()
		                          .equals(Flux.just(1)
		                                      .hide()
		                                      .publish()
		                                      .getClass()));
	}
}