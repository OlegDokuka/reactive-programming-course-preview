import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;

public class Task {

	public static Publisher<String> replayLast3ElementsInHotFashion1(Flux<String> coldSource) {
		return Flux.error(new ToDoException()); // With Operator
	}

	public static Publisher<String> replayLast3ElementsInHotFashion2(Flux<String> coldSource) {
		final Sinks.Many<String> replaySink = Sinks.many().replay().limit(3);

Flux.error(new ToDoException()); // With Processor
	}
}