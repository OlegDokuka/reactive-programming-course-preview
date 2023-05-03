import reactor.core.publisher.Mono;

public interface Validator {
    Mono<Void> validate();
}
