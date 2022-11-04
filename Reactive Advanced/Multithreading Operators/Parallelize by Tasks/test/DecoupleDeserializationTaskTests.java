import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class DecoupleDeserializationTaskTests {

	@Test
	public void checkDeserializationOnDifferentThread() {
		Thread[] threads = new Thread[2];
		Deserializer mock = Mockito.mock(Deserializer.class);
		Mockito.when(mock.deserialize(Mockito.any())).thenAnswer(a -> {
			threads[1] = Thread.currentThread();
			return new Event(a.getArgument(0));
		});
		DecoupleDeserializationTask.deserializer = mock;
		StepVerifier
				.create(
						Flux.defer(() -> {
								threads[0] = Thread.currentThread();
								return Flux.just("Hello");
							})
							.transform(DecoupleDeserializationTask::handleDeseriallization)
				)
				.expectSubscription()
				.expectNext(new Event("Hello"))
				.verifyComplete();

		Assertions.assertThat(threads[0])
		          .isNotNull()
		          .isNotEqualTo(threads[1]);
		Assertions.assertThat(threads[1])
		          .isNotNull();
	}
}