import reactor.core.publisher.Mono;

import java.security.SecureRandom;

public class ReactiveRandom {
    public Mono<Integer> nextInt() {
        return Mono.fromCallable(() -> SecureRandom.getInstanceStrong().nextInt());
    }
}
