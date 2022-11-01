import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.UnicastProcessor;
import reactor.test.StepVerifier;

public class HnSTask3Tests {

	@Test
	public void testSolution() {
		UnicastProcessor<String> source = UnicastProcessor.create();
		ReplayProcessor<String> consumer1 = ReplayProcessor.create(10);
		ReplayProcessor<String> consumer2 = ReplayProcessor.create(10);

		Publisher<String> publisher = Task.transformToHotUsingProcessor(source);

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
}