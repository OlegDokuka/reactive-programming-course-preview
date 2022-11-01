import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MultithreadingOperatorsTask3Tests {

	@Test
	public void testSolution() {
		int availableProcessors = Runtime.getRuntime()
		                                 .availableProcessors();
		ConcurrentHashMap<Thread, Integer> concurrentHashMap = new ConcurrentHashMap<>();
		StepVerifier.create(Task.paralellizeWorkOnDifferentThreads(Flux.range(0,
				availableProcessors))
		                        .doOnNext(s -> {
		                        	concurrentHashMap.put(Thread.currentThread(), s);
			                        try {
				                        Thread.sleep(500);
			                        }
			                        catch (InterruptedException e) {
				                        e.printStackTrace();
			                        }
		                        }))
		            .expectSubscription()
		            .expectNextCount(availableProcessors)
		            .expectComplete()
		            .verify(Duration.ofMillis(2000));

		Assertions.assertThat(concurrentHashMap)
		          .hasSize(availableProcessors);
	}
}