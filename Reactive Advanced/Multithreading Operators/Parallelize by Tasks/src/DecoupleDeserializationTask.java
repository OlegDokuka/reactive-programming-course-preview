import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class DecoupleDeserializationTask {

	static Deserializer<String, Event> deserializer;

	public static Flux<Event> handleDeseriallization(Flux<String> source) {
		return Flux.error(new ToDoException());
	}
}