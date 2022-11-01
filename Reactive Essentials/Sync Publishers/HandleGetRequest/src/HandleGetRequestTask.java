import reactor.core.publisher.Mono;

public class HandleGetRequestTask {

	public static final IllegalArgumentException UNSUPPORTED_HTTP_METHOD =
			new IllegalArgumentException("Unsupported HTTP method. Expected method GET");

	public static ReactiveRandom reactiveRandom;

	public static int getRandomNumberHandler(RequestEntity<?> request) {
		if (request.method != HttpMethod.GET) {
			throw UNSUPPORTED_HTTP_METHOD;
		 }

		return reactiveRandom.nextInt().block();
	}
}