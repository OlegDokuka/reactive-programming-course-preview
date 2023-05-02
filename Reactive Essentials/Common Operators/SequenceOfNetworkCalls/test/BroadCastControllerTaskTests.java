import java.nio.ByteBuffer;
import java.time.Duration;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BroadCastControllerTaskTests {

	@Test
	public void testResultIsCorrect() {
		HttpClient httpClient = Mockito.mock(HttpClient.class);
		DiscoveryService discoveryService = Mockito.mock(DiscoveryService.class);


		Mockito.when(httpClient.postEntityAsync(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenAnswer(a -> Mono.delay(Duration.ofMillis(100)).toFuture())
				.thenAnswer(a -> Mono.delay(Duration.ofMillis(200)).toFuture())
				.thenAnswer(a -> Mono.delay(Duration.ofMillis(300)).toFuture());

		Mockito.when(discoveryService.asyncDiscoverAddressesFor(Mockito.any()))
				.thenReturn(CompletableFuture.completedFuture(Arrays.asList("a", "b", "c")));


		BroadCastControllerTask.httpClient = httpClient;
		BroadCastControllerTask.discoveryService = discoveryService;

		ByteBuffer fakeBuf = Mockito.mock(ByteBuffer.class);

		StepVerifier.withVirtualTime(() -> BroadCastControllerTask.broadcast(fakeBuf))
				.expectSubscription()
				.expectNoEvent(Duration.ofMillis(100))
				.expectNoEvent(Duration.ofMillis(200))
				.expectNoEvent(Duration.ofMillis(300))
				.as("Expect Sequential execution using concatMap")
				.expectComplete()
				.verify();
	}
}