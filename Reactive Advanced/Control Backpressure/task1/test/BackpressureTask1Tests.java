import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

public class BackpressureTask1Tests {

	@Test
	public void mergeSeveralSourcesTest() {
		TestPublisher<RefCounted> processor = TestPublisher.create();
		StepVerifier
				.create(Task.dropElementsOnBackpressure(processor.flux()), 0)
				.expectSubscription()
				.then(() -> processor.next(new TestRefCounted("")))
				.then(() -> processor.next(new TestRefCounted("")))
				.thenRequest(1)
				.then(() -> processor.next(new TestRefCounted("0")))
				.expectNext(new TestRefCounted("0"))
				.then(() -> processor.next(new TestRefCounted("0")))
				.then(() -> processor.next(new TestRefCounted("0")))
				.thenRequest(1)
				.then(() -> processor.next(new TestRefCounted("10")))
				.expectNext(new TestRefCounted("10"))
				.thenRequest(1)
				.then(() -> processor.next(new TestRefCounted("20")))
				.expectNext(new TestRefCounted("20"))
				.then(() -> processor.next(new TestRefCounted("40")))
				.then(() -> processor.next(new TestRefCounted("30")))
				.then(processor::complete)
				.expectComplete()
				.verifyThenAssertThat()
				.hasDiscarded(
					new TestRefCounted("", 0),
					new TestRefCounted("", 0),
					new TestRefCounted("0", 0),
					new TestRefCounted("0", 0),
					new TestRefCounted("40", 0),
					new TestRefCounted("30", 0)
				);
	}

	static class TestRefCounted extends AtomicLong implements RefCounted {

		final String value;

		public TestRefCounted(String value) {
			super(1);
			this.value = value;
		}

		public TestRefCounted(String value, long refCnt) {
			super(refCnt);
			this.value = value;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			TestRefCounted counted = (TestRefCounted) o;

			return value.equals(counted.value) && Objects.equals(get(), counted.get());
		}

		@Override
		public int hashCode() {
			return value.hashCode();
		}

		@Override
		public long refCount() {
			return get();
		}

		@Override
		public void release() {
			long l = decrementAndGet();

			if (l < 0) {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public String toString() {
			return "TestRefCounted{" + "value='" + value + '\'' + ", refCnt=" + get() + '}';
		}
	}
}