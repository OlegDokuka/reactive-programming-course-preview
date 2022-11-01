import reactor.core.publisher.Mono;

public class GenerateUUIDTask {

	static UUIDGenerator uuidGenerator;

	public static String generateRandomUUID() {
		return uuidGenerator.secureUUID();
	}
}