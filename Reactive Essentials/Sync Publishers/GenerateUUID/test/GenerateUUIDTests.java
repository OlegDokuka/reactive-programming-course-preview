import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class GenerateUUIDTests {

	@Test
	public void testSolution() {
		GenerateUUIDTask.uuidGenerator = Mockito.mock(UUIDGenerator.class);
		Mockito.when(GenerateUUIDTask.uuidGenerator.secureUUID()).thenReturn("test-uuid");

		Object sequence = GenerateUUIDTask.generateRandomUUID();

		Mockito.verify(GenerateUUIDTask.uuidGenerator, Mockito.times(0)).secureUUID();

		if (!(sequence instanceof Mono)) {
			Assertions.fail("You need to refactor API so it uses Reactive API properly");
		}

		StepVerifier.create(((Mono<String>) sequence))
					.expectNext("test-uuid")
		            .expectComplete()
		            .verify(Duration.ofMillis(100));
	}
}