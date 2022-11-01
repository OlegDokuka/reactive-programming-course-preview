import java.time.Duration;
import java.util.function.Function;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

public class Task {

	public static void unitTestAFunction(Function<Flux<String>, Flux<Long>> functionToTest) {
		testSuccessCase(functionToTest);
		testFailureCase(functionToTest);
	}

	static void testSuccessCase(Function<Flux<String>, Flux<Long>> functionToTest) {
		// produce "1" "2" "100"
		throw new ToDoException()); // TODO Check Success Case;
	}

	static void testFailureCase(Function<Flux<String>, Flux<Long>> functionToTest) {
		// produce non number string and check NumberFormatException is produced
		throw new ToDoException()); // TODO Check Failure Case;
	}
}