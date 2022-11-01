import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MultipleSourcesTask9Tests {

	@Test
	public void testSolution() throws URISyntaxException {
		URI resourceUri = ClassLoader.getSystemResource("testfile.txt").toURI();
		Object source = TextFileStreamTask.readFile(Paths.get(resourceUri).toAbsolutePath().toString());

		if (!(source instanceof Flux)) {
			Assertions.fail("Unexpected return type");
		}

		StepVerifier.create(((Flux<String>) source))
		            .expectSubscription()
		            .expectNextCount(5)
		            .verifyComplete();
	}
}