package service.external;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.UnicastProcessor;
import reactor.test.StepVerifier;

public class CryptoServiceTests {

	@Test
	public void verifyThatSupportMultiSubscribers() {
		AtomicInteger subscribtions = new AtomicInteger(0);
		Flux<Object> source = DirectProcessor
				.create()
				.doOnSubscribe(s -> subscribtions.incrementAndGet());

		Flux<Object> cachedFlux = CryptoCompareService.provideCaching(source);

		cachedFlux.subscribe(System.out::println);
		cachedFlux.subscribe(System.out::println);

		Assertions.assertThat(subscribtions.get()).isEqualTo(1);
	}

	@Test
	public void verifyThatSupportIsolationAndResilience() {
		Flux<String> source = Flux.defer(() -> Flux.just("1", "2", "3")
		                                           .mergeWith(Flux.error(new RuntimeException())));

		StepVerifier.withVirtualTime(() ->
				CryptoCompareService.provideResilience(source)
				                    .take(6)
		)
		            .expectSubscription()
		            .thenAwait(Duration.ofDays(10))
		            .expectNext("1", "2", "3")
		            .expectNext("1", "2", "3")
		            .expectComplete()
		            .verify();

	}

	@Test
	public void verifyThatSupportReplayMode() {
		UnicastProcessor<String> source = UnicastProcessor.create();
		ReplayProcessor<String> consumer1 = ReplayProcessor.create(10);
		ReplayProcessor<String> consumer2 = ReplayProcessor.create(10);

		Publisher<String> publisher = CryptoCompareService.provideCaching(source);

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
		            .verifyComplete();

		StepVerifier.create(consumer2)
		            .expectSubscription()
		            .expectNext("D", "E", "F", "G")
		            .verifyComplete();
	}
}