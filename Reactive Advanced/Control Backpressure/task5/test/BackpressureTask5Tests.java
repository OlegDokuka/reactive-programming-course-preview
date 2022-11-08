import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class BackpressureTask5Tests {

	@Test
	public void testSolution() throws InterruptedException {
		int size = 100000;
		long requests = (long) Math.ceil((Math.log(size) / Math.log(2) + 1e-10));
		CountDownLatch latch = new CountDownLatch(1);
		AtomicInteger iterations = new AtomicInteger();

		Task.dynamicDemand(Flux.range(0, size)
		                       .map(String::valueOf)
		                       .publishOn(Schedulers.single())
		                       .doOnRequest(r -> iterations.incrementAndGet()), latch);

		latch.await(5, TimeUnit.SECONDS);

		Assertions.assertThat(requests)
		          .isEqualTo(iterations.get());
	}
}