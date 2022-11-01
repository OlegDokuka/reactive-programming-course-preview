import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Connection<T> {

	Mono<Void> close();

	Mono<Void> rollback();

	Mono<Long> write(Flux<T> data);
}
