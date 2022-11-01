import java.io.File;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class CommonPublishersTask5Tests {

	@Test
	public void testSolution() {
		Object file = CreateTempFileTask.createTempFile("test", null);

		if (!(file instanceof Mono)) {
			((File) file).deleteOnExit();
		}

		StepVerifier.create(((Mono<File>) file))
		            .expectNextMatches(f -> {
						boolean exists = f.exists();
						f.deleteOnExit();
						return exists;
					})
		            .expectComplete()
		            .verify(Duration.ofMillis(100));
	}
}