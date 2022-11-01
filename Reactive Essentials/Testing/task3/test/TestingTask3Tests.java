import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class TestingTask3Tests {

	@Test
	public void testSolution() throws InterruptedException {
		AtomicLong calls = new AtomicLong();
		BlockingQueue<Exception> exceptionBlockingQueue = new ArrayBlockingQueue<>(100);
		Function<Flux<String>, Flux<Long>> testFunction = f -> {
			calls.incrementAndGet();
			return f.map(s -> {
				try {
					return Long.parseLong(s);
				} catch (NumberFormatException e) {
					exceptionBlockingQueue.add(e);
					throw e;
				}
			});
		};
		Task.unitTestAFunction(testFunction);

		Assertions.assertThat(calls.get())
		          .as("Make sure all cases are covered. At least should be a successful" +
				          " case and a failure case")
		          .isGreaterThanOrEqualTo(2);

		Assertions.assertThat(exceptionBlockingQueue)
		          .as("Make sure failure case is tested")
		          .hasAtLeastOneElementOfType(NumberFormatException.class);
	}
}