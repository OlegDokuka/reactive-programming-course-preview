import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HttpClient {

	Mono<Void> send(Flux<OrderedByteBuffer> value);
}
