import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.UnicastProcessor;
import reactor.test.StepVerifier;

public class HnSTask1Tests {

	@Test
	public void testSolution() {
		ArrayList<Publisher> operators = new ArrayList<>();
		Hooks.onEachOperator(e -> {
			operators.add(e);
			return e;
		});

		try {
			Sinks.Many<String> source = Sinks
					.unsafe()
					.many()
					.unicast()
					.onBackpressureBuffer();
			ReplayProcessor<String> consumer1 = ReplayProcessor.create(10);
			ReplayProcessor<String> consumer2 = ReplayProcessor.create(10);
			ReplayProcessor<String> consumer3 = ReplayProcessor.create(10);

			Publisher<String> publisher = Task.transformToHotWithOperator(source.asFlux());

			publisher.subscribe(consumer1);

			source.emitNext("A", Sinks.EmitFailureHandler.FAIL_FAST);
			source.emitNext("B", Sinks.EmitFailureHandler.FAIL_FAST);
			source.emitNext("C", Sinks.EmitFailureHandler.FAIL_FAST);

			publisher.subscribe(consumer2);

			source.emitNext("D", Sinks.EmitFailureHandler.FAIL_FAST);
			source.emitNext("E", Sinks.EmitFailureHandler.FAIL_FAST);
			source.emitNext("F", Sinks.EmitFailureHandler.FAIL_FAST);

			source.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST);

			publisher.subscribe(consumer3);

			StepVerifier.create(consumer1)
			            .expectSubscription()
			            .expectNext("A", "B", "C", "D", "E", "F")
			            .verifyComplete();

			StepVerifier.create(consumer2)
			            .expectSubscription()
			            .expectNext("A", "B", "C", "D", "E", "F")
			            .verifyComplete();

			StepVerifier.create(consumer3)
			            .expectSubscription()
			            .expectNext("A", "B", "C", "D", "E", "F")
			            .verifyComplete();
		}
		finally {
			Hooks.resetOnEachOperator();
		}

		Assertions.assertThat(operators)
		          .as("Expected usage of built-in operator")
		          .anyMatch(p -> p.getClass()
		                          .equals(Flux.just(1)
		                                      .hide()
		                                      .publish()
		                                      .getClass()));
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
			EmitterProcessor<String> consumer1 = EmitterProcessor.create(true);
			EmitterProcessor<String> consumer2 = EmitterProcessor.create(true);
			BaseSubscriber<String> consumer3 = new BaseSubscriber<String>() {
			};

			Publisher<String> publisher = Task.transformToHotWithOperator(source);

			publisher.subscribe(consumer1);

			source.onNext("A");
			source.onNext("B");
			source.onNext("C");

			publisher.subscribe(consumer2);

			source.onNext("D");
			source.onNext("E");
			source.onNext("F");

			publisher.subscribe(consumer3);

			consumer3.dispose();

			StepVerifier.create(consumer1)
			            .expectSubscription()
			            .expectNext("A", "B", "C", "D", "E", "F")
			            .thenCancel()
			.verify();

			StepVerifier.create(consumer2)
			            .expectSubscription()
			            .expectNext("A", "B", "C", "D", "E", "F")
			            .thenCancel()
			.verify();

			Assertions.assertThat(consumer3.isDisposed())
			          .isTrue();

			Assertions.assertThat(source.isDisposed())
			          .isTrue();
		}
		finally {
			Hooks.resetOnEachOperator();
		}

		Assertions.assertThat(operators)
		          .as("Expected usage of built-in operator")
		          .anyMatch(p -> p.getClass()
		                          .equals(Flux.just(1)
		                                      .hide()
		                                      .publish()
		                                      .getClass()));
	}
}