import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class NonBlockingGenerateUUIDTask {

	static UUIDGenerator uuidGenerator;

	public static Mono<String> generateRandomUUID() {
		return Mono.defer(() -> Mono.just(uuidGenerator.secureUUID()));
	}
}