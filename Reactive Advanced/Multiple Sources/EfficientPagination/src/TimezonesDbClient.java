import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;

public class TimezonesDbClient {

	final String            apiKey;
	final AsyncRestTemplate asyncRestTemplate;

	public TimezonesDbClient(String apiKey, AsyncRestTemplate asyncRestTemplate) {
		this.apiKey = apiKey;
		this.asyncRestTemplate = asyncRestTemplate;
	}

	public Mono<List<TimezonedbResponseZone>> findPageOfTimezonesFor(String cityName) {
		return Mono.error(new ToDoException("Provide Pagination iml"));
	}

	Mono<TimezonedbResponse> findPageOfTimezonesFor(String cityName, int page) {
		return Mono.fromFuture(() -> asyncRestTemplate.getForObjectAsync(
				           "https://vip.timezonedb.com/v2.1/get-time-zone?key={key}&format=json&by=city&country=US&city={city}&page={page}",
				           TimezonedbResponse.class,
				           apiKey,
				           cityName,
				           page));
	}
}
