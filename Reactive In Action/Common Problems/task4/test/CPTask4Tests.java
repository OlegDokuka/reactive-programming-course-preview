import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class CPTask4Tests {

	@Test
	public void uploadTest() {
		TrickyHttpClient client = new TrickyHttpClient();
		DataUploaderService service = new DataUploaderService(client);
		StepVerifier.withVirtualTime(() -> service.upload(Flux.range(0, 1000)
		                                                      .map(i -> new OrderedByteBuffer(
				                                                      i,
				                                                      ByteBuffer.allocate(
						                                                      i)))
		                                                      .window(100)
		                                                      .delayElements(Duration.ofMillis(
				                                                      1500))
		                                                      .flatMap(Function.identity())))
		            .expectSubscription()
		            .thenAwait(Duration.ofSeconds(1000))
		            .verifyComplete();

		verifyOrdered(client);
		verifyTimeout(client);
	}

	void verifyOrdered(TrickyHttpClient client) {
		List<OrderedByteBuffer> recorded = client.getRecordedBuffers();

		for (int i = 0; i < recorded.size(); i++) {
			Assertions.assertThat(recorded.get(i)
			                              .getWritePosition())
			          .as("Recorded elements are in the wrong order, " + "consider concatMap instead of flatMap")
			          .isEqualTo(i);
		}
	}

	void verifyTimeout(TrickyHttpClient client) {
		Assertions.assertThat(client.getRecords()
		                            .stream()
		                            .anyMatch(l -> l.size() > 1))
		          .isTrue();
	}
}