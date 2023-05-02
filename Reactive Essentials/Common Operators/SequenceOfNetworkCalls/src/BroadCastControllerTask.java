import java.nio.ByteBuffer;

import java.util.List;
import java.util.function.Function;
import reactor.core.publisher.Mono;

public class BroadCastControllerTask {

	static HttpClient httpClient;
	static DiscoveryService discoveryService;

	public static void broadcast(ByteBuffer payload) {
		List<String> test = discoveryService.discoverAddressesFor("test");
    for (String endpoint : test) {
        httpClient.postEntity(endpoint + "/api/push", payload, Void.class);
    }

	}
}