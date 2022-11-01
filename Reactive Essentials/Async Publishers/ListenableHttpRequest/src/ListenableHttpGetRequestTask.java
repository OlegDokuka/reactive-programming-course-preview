import java.util.stream.Stream;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ListenableHttpGetRequestTask {

	static AsyncHttpClient asyncHttpClient;

	public static ListenableFuture<String> getLorem() {
		return asyncHttpClient.getForObject("https://baconipsum.com/api/?type=meat-and-filler", String.class);
	}
}