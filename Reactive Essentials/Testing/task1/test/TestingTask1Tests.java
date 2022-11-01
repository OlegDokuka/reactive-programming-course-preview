import java.util.Random;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.publisher.TestPublisher;

public class TestingTask1Tests {

	@Test
	public void testSolution() {
		TestPublisher<Integer> cold = TestPublisher.createCold();

		Flux.fromStream(new Random().ints()
		                            .boxed())
		    .take(15)
		    .skip(5)
		    .subscribe(cold::next, cold::error, cold::complete);

		Task.verifyThat10ElementsEmitted(cold.flux());

		cold.assertWasSubscribed();
		cold.assertWasRequested();
		cold.assertWasNotCancelled();
	}
}