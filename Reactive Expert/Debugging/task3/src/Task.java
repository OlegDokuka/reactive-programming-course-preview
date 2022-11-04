import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Task {

	public static Flux<Long> metricsTask(Flux<Long> flux) {
		// TODO scheduler metrics
		return flux
				// TODO add metrics for flux and name your flux as "MyFlux"
				.subscribeOn(Schedulers.parallel())
				.publishOn(Schedulers.single())
				.log("After PublisherOn");
	}
}