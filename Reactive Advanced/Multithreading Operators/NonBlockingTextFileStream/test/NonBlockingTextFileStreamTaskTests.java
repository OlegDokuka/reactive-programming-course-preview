import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class NonBlockingTextFileStreamTaskTests {

	@Test
	public void testSolution() throws URISyntaxException {
		Thread[] threads = new Thread[1];
		Files mock = Mockito.mock(Files.class);
		Mockito.when(mock.lines(Mockito.any())).thenAnswer(a -> {
			threads[0] = Thread.currentThread();
			return java.nio.file.Files.lines(a.getArgument(0));
		});
		NonBlockingTextFileStreamTask.files = mock;
		URI resourceUri = ClassLoader.getSystemResource("testfile.txt").toURI();
		Object source = NonBlockingTextFileStreamTask.readFile(Paths.get(resourceUri).toAbsolutePath().toString());

		if (!(source instanceof Flux)) {
			Assertions.fail("Unexpected return type");
		}

		StepVerifier.create(((Flux<String>) source))
		            .expectSubscription()
		            .expectNextCount(5)
		            .verifyComplete();


		org.assertj.core.api.Assertions.assertThat(threads[0])
				.isNotEqualTo(Thread.currentThread());
	}
}