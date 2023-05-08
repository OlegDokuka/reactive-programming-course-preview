import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.function.IntFunction;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public class LogsObserverTask {

	static DiscoveryService discoveryService;

	public static Flux<String> observeLogs() {
		return Flux.error(new ToDoException());
	}

	static long extractTimeFromLog(String logLine) {
		int start = logLine.indexOf("[") + 1;
		int end = logLine.indexOf("]");

		return LocalTime.parse(logLine.substring(start, end),
				                DateTimeFormatter.ISO_LOCAL_TIME)
		                .toNanoOfDay();
	}
}