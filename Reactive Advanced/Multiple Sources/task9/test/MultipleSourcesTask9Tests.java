import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class MultipleSourcesTask9Tests {

	@Test
	public void testSolution() throws URISyntaxException {
		URI resourceUri = ClassLoader.getSystemResource("testfile.txt").toURI();

		StepVerifier.create(Task.readFile(Paths.get(resourceUri).toAbsolutePath().toString()))
		            .expectSubscription()
		            .expectNextCount(5)
		            .verifyComplete();
	}
}