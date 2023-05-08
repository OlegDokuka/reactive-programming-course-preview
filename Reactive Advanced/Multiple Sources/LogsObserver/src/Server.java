import reactor.core.publisher.Flux;

public interface Server {
	Flux<String> logsStream();
}
