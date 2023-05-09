import reactor.core.publisher.Mono;

public class FutureHttpGetRequestTask {

	static AsyncRestTemplate asyncRestTemplate;

	public static CompletableFuture<String> getLorem() {
		return asyncRestTemplate.getForObject("https://baconipsum.com/api/?type=meat-and-filler", String.class);
	}
}