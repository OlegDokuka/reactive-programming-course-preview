import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ContextTask2Tests {

	@Test
	public void testSolution() {
		StepVerifier.create(Task.provideCorrectContext(Mono.just("Test"), "Key", "Value"))
		            .expectSubscription()
		            .expectAccessibleContext()
		            .contains("Key", "Value")
		            .then()
		            .expectNext("Test")
		            .verifyComplete();
	}
}