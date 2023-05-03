import java.util.HashMap;
import java.util.Map;
import reactor.core.publisher.Mono;

public class VisitorsByCountryTask {

	static VisitorsRepository visitorsRepository;

	public static Mono<Map<String, Long>> countVisitorsByCountry() {
		return Mono.error(new ToDoException());
	}
}