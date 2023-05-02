import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class CommonOperatorsTask11Tests {

	@Test
	public void testSolution() {
		Task.fizzBuzz(Flux.range(1, 100))
		    .zipWith(Flux.range(1, 100),
				    (word, index) -> new Task.IndexedWord(index, word))
		    .subscribe(new FizzBuzzVerifier());
	}

	static class FizzBuzzVerifier extends BaseSubscriber<Task.IndexedWord> {

		private final AtomicInteger counter = new AtomicInteger();

		@Override
		public void hookOnComplete() {
			if (counter.get() < 100) {
				Assertions.fail("Unexpected termination, should be 100 elements emitted");
			}
		}

		@Override
		public void hookOnError(Throwable e) {
			Assertions.fail("Unexpected throwable [" + e + "]");
		}

		@Override
		public void hookOnNext(Task.IndexedWord indexedWord) {
			if (indexedWord.getIndex() % 5 == 0 && indexedWord.getIndex() % 3 == 0) {
				Assertions.assertThat(indexedWord.getWord())
				          .as("Should equal to FizzBuzz, but was [" + indexedWord.getWord() + "] instead")
				          .isEqualTo("FizzBuzz");
			}
			else if (indexedWord.getIndex() % 3 == 0) {
				Assertions.assertThat(indexedWord.getWord())
				          .as("Should equal to Fizz, but was [" + indexedWord.getWord() + "] instead")
				          .isEqualTo("Fizz");
			}
			else if (indexedWord.getIndex() % 5 == 0) {
				Assertions.assertThat(indexedWord.getWord())
				          .as("Should equal to Buzz, but was [" + indexedWord.getWord() + "] instead")
				          .isEqualTo("Buzz");
			}
			else {
				try {
					Integer.valueOf(indexedWord.getWord())
					       .toString();
				}
				catch (Exception e) {
					Assertions.fail("Should be mapped to Number, but was [" + indexedWord.getWord() + "] instead");
				}
			}

			counter.incrementAndGet();
		}
	}
}