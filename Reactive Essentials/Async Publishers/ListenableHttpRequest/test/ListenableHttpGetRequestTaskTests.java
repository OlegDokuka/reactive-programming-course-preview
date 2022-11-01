import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ListenableHttpGetRequestTaskTests {
	static final BiConsumer CANCELLED = new BiConsumer() {
		@Override
		public void accept(Object o, Object o2) {

		}
	};

	@Test
	public void testSolution() {
		AsyncHttpClient mock = Mockito.mock(AsyncHttpClient.class);
		final AtomicReference<BiConsumer> handlerSetter = new AtomicReference();
		ListenableFuture<String> future = new ListenableFuture<String>() {

			@Override
			public void handle(BiConsumer<String, Throwable> handler) {
				Objects.requireNonNull(handler);
				if (!handlerSetter.compareAndSet(null, handler)) {
					Assertions.fail("can not be set twice");
				}
			}

			@Override
			public void cancel() {
				Assertions.assertThat(handlerSetter.getAndSet(CANCELLED)).isNotNull();
			}
		};
		Mockito.when(mock.<String>getForObject(Mockito.anyString(), Mockito.any())).thenReturn(future);
		ListenableHttpGetRequestTask.asyncHttpClient = mock;
		Object sequence = ListenableHttpGetRequestTask.getLorem();

		if (sequence instanceof Mono) {
			StepVerifier.create(((Mono<String>) sequence))
					.expectSubscription()
					.then(() -> handlerSetter.get().accept("lorem", null))
					.expectNext("lorem")
					.expectComplete()
					.verify(Duration.ofMillis(100));
		}
	}

	@Test
	public void testSolution2() {
		AsyncHttpClient mock = Mockito.mock(AsyncHttpClient.class);
		final AtomicReference<BiConsumer> handlerSetter = new AtomicReference();
		ListenableFuture<String> future = new ListenableFuture<String>() {

			@Override
			public void handle(BiConsumer<String, Throwable> handler) {
				Objects.requireNonNull(handler);
				if (!handlerSetter.compareAndSet(null, handler)) {
					Assertions.fail("can not be set twice");
				}
			}

			@Override
			public void cancel() {
				Assertions.assertThat(handlerSetter.getAndSet(CANCELLED)).isNotNull();
			}
		};
		Mockito.when(mock.<String>getForObject(Mockito.anyString(), Mockito.any())).thenReturn(future);
		ListenableHttpGetRequestTask.asyncHttpClient = mock;
		Object sequence = ListenableHttpGetRequestTask.getLorem();

		if (sequence instanceof Mono) {
			StepVerifier.create(((Mono<String>) sequence))
					.expectSubscription()
					.then(() -> handlerSetter.get().accept(null, new RuntimeException("boo")))
					.expectErrorMessage("boo")
					.verify(Duration.ofMillis(100));
		}
	}

	@Test
	public void testSolution3() {
		AsyncHttpClient mock = Mockito.mock(AsyncHttpClient.class);
		final AtomicReference<BiConsumer> handlerSetter = new AtomicReference();
		ListenableFuture<String> future = new ListenableFuture<String>() {

			@Override
			public void handle(BiConsumer<String, Throwable> handler) {
				Objects.requireNonNull(handler);
				if (!handlerSetter.compareAndSet(null, handler)) {
					Assertions.fail("can not be set twice");
				}
			}

			@Override
			public void cancel() {
				Assertions.assertThat(handlerSetter.getAndSet(CANCELLED)).isNotNull();
			}
		};
		Mockito.when(mock.<String>getForObject(Mockito.anyString(), Mockito.any())).thenReturn(future);
		ListenableHttpGetRequestTask.asyncHttpClient = mock;
		Object sequence = ListenableHttpGetRequestTask.getLorem();

		if (sequence instanceof Mono) {
			StepVerifier.create(((Mono<String>) sequence))
					.expectSubscription()
					.thenCancel()
					.verify(Duration.ofMillis(100));
		}

		Assertions.assertThat(handlerSetter.get()).isEqualTo(CANCELLED);
	}
}