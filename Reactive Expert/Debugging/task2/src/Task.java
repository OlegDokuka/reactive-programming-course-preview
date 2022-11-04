import java.io.File;
import java.time.Duration;
import java.util.logging.Level;

import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

public class Task {

	static {
		BlockHound.install();
	}


	static Mono<File> createTempFileReactively() {
		Flux.defer(() -> {
			System.out.println("generate data here");
			return Flux.just("data");
		});
		return Mono.fromCallable(() -> File.createTempFile("bla", "bla"));
	}

	public static Flux<Long> loggerTask(Flux<Long> flux) {

		Mono.delay(Duration.ofMillis(100))
				.flatMap(delayed -> createTempFileReactively())
				.block();

		return flux
				// TODO add log here. Log only requests
				.subscribeOn(Schedulers.parallel())
				.publishOn(Schedulers.single());
				// TODO add log here.
	}
}