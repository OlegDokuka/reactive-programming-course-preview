import java.time.Duration;
import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.UnicastProcessor;
import reactor.test.StepVerifier;

public class HnSTask2Tests {

	@Test
	public void testSolution() {
		ArrayList<Publisher> operators = new ArrayList<>();
		Hooks.onEachOperator(e -> {
			operators.add(e);
			return e;
		});

		try {

			UnicastProcessor<String> source = UnicastProcessor.create();
			ReplayProcessor<String> consumer1 = ReplayProcessor.create(10);
			ReplayProcessor<String> consumer2 = ReplayProcessor.create(10);

			Publisher<String> publisher = Task.replayLast3ElementsInHotFashion1(source);

			source.onNext("A");
			source.onNext("B");
			source.onNext("C");

			publisher.subscribe(consumer1);

			source.onNext("D");
			source.onNext("E");
			source.onNext("F");

			publisher.subscribe(consumer2);

			source.onNext("G");

			source.onComplete();

			StepVerifier.create(consumer1)
			            .expectSubscription()
			            .expectNext("A", "B", "C", "D", "E", "F", "G")
			            .expectComplete()
			            .verify(Duration.ofMillis(1000));

			StepVerifier.create(consumer2)
			            .expectSubscription()
			            .expectNext("D", "E", "F", "G")
			            .expectComplete()
			            .verify(Duration.ofMillis(1000));
		} finally {
			Hooks.resetOnEachOperator();
		}

		Assertions.assertThat(operators)
		          .as("Expected usage of built-in operator")
		          .anyMatch(p -> p.getClass().equals(Flux.just(1).hide().replay().getClass()));
	}


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

			Publisher<String> publisher = Task.replayLast3ElementsInHotFashion2(source);

			source.onNext("A");
			source.onNext("B");
			source.onNext("C");

			publisher.subscribe(consumer1);

			source.onNext("D");
			source.onNext("E");
			source.onNext("F");

			publisher.subscribe(consumer2);

			source.onNext("G");

			source.onComplete();

			StepVerifier.create(consumer1)
			            .expectSubscription()
			            .expectNext("A", "B", "C", "D", "E", "F", "G")
			            .expectComplete()
			            .verify(Duration.ofMillis(1000));

			StepVerifier.create(consumer2)
			            .expectSubscription()
			            .expectNext("D", "E", "F", "G")
			            .expectComplete()
			            .verify(Duration.ofMillis(1000));
		} finally {
			Hooks.resetOnEachOperator();
		}

		Assertions.assertThat(operators)
		          .as("Expected usage of a processor")
		          .noneMatch(p -> p.getClass().equals(Flux.just(1).hide().replay().getClass()));
	}
}