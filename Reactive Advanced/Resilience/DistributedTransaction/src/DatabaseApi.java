import reactor.core.publisher.Mono;

public interface DatabaseApi {

	public <T> Mono<Connection<T>> open();

	public Mono<Void> rollbackTransaction(long id);
}
