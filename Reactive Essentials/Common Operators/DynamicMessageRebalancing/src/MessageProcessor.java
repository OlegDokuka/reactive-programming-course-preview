import reactor.core.publisher.Flux;

public interface MessageProcessor {

	Flux<Object> process(Flux<Object> flux);
}
