import reactor.core.publisher.Flux;

import java.time.Duration;

public class VisitorsByPeriodTask {

	static VisitorsService visitorsService;

	public static Flux<Long> countVisitorsByCountry(Duration duration) {
		return Flux.error(new ToDoException());
	}
}