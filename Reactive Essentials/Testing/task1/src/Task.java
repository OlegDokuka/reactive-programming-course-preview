import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Task {

	public static void verifyThat10ElementsEmitted(Flux<Integer> flux) {
		throw new ToDoException(); // use StepVerifier to perform testing
	}
}