import java.util.concurrent.Callable;

import reactor.blockhound.BlockHound;
import reactor.blockhound.integration.BlockHoundIntegration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

@SuppressWarnings({"ConstantConditions", "BlockingMethodInNonBlockingContext"})
public class Task {

	public static Flux<Long> checkAndDebug(Flux<Long> flux) {
		BlockHound.install(/*Add BlockHound fix*/);
		// 1) Add Better error printing
		return flux
				// FIXME
				.scan(0L, (aLong, aLong2) -> (aLong + aLong2 + 2 * aLong) / aLong2)
				// FIXME
				.flatMap(Task::doWork)
				.log()
				.retry(5);
	}

	private static Mono<Long> doWork(Long e) {
		return Mono.fromCallable(new MyCallable())
		           .zipWith(Mono.just(e))
				   .map(t2 -> t2.getT1() / t2.getT2());
	}

	// Add BlockHound Integration

	public static class MyCallable implements Callable<Long> {

		@Override
		public Long call() throws Exception {
			Thread.sleep(100);
			return 1L;
		}
	}
}