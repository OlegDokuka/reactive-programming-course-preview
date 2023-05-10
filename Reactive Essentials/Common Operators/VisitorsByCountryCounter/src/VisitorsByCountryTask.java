import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;

public class VisitorsByCountryTask {

	static VisitorsRepository visitorsRepository;

	public static Mono<Map<String, Long>> countVisitorsByCountry() {
		return Mono.error(new ToDoException());
	}
}