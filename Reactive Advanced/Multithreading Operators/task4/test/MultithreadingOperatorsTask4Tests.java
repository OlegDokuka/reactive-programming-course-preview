import java.time.Duration;
import java.util.LinkedList;
import java.util.Queue;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MultithreadingOperatorsTask4Tests {

	@Test
	public void testSolution() {
		Queue<Thread> threadsQueueOnGenerate = new LinkedList<>();
		Queue<Thread> threadsQueueOnWork1 = new LinkedList<>();
		Queue<Thread> threadsQueueOnWork2 = new LinkedList<>();
		StepVerifier
				.create(Task.modifyStreamExecution(
					Flux.<Long, Long>generate(() -> 0L, (s, sink) -> {
						sink.next(s);
						threadsQueueOnGenerate.add(Thread.currentThread());
						return s + 1;
					}).take(10000),
					(e) -> {
						threadsQueueOnWork1.add(Thread.currentThread());
						return e;
					},
					(e) -> {
						threadsQueueOnWork2.add(Thread.currentThread());
						return e;
					}
				).limitRate(32))
	            .expectSubscription()
	            .expectNextCount(10000)
	            .expectComplete()
	            .verify(Duration.ofMillis(2000));

		Assertions.assertThat(threadsQueueOnGenerate)
		          .hasSameSizeAs(threadsQueueOnWork1)
		          .containsExactlyElementsOf(threadsQueueOnWork1)
				  .allMatch(t -> t.getName().startsWith("single-"));

		Assertions.assertThat(threadsQueueOnWork2)
		          .doesNotContainAnyElementsOf(threadsQueueOnGenerate)
		          .allMatch(t -> t.getName().startsWith("parallel-"));

	}
}