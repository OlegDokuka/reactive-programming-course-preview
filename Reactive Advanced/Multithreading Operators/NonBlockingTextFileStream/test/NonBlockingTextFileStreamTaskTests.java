import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class NonBlockingTextFileStreamTaskTests {

	static {
		BlockHound.install();
	}

	@Test
	public void testSolution() throws URISyntaxException {
		Thread[] threads = new Thread[1];
		Thread[] originThreads = new Thread[1];
		Files mock = Mockito.mock(Files.class);
		Mockito.when(mock.lines(Mockito.any())).thenAnswer(a -> {
			threads[0] = Thread.currentThread();
			Thread.yield();
			return java.nio.file.Files.lines(a.getArgument(0));
		});
		NonBlockingTextFileStreamTask.files = mock;
		URI resourceUri = ClassLoader.getSystemResource("testfile.txt").toURI();
		Object source = NonBlockingTextFileStreamTask.readFile(Paths.get(resourceUri).toAbsolutePath().toString());

		if (!(source instanceof Flux)) {
			Assertions.fail("Unexpected return type");
		}

		StepVerifier.create(((Flux<String>) source)
						.doFirst(() -> originThreads[0] = Thread.currentThread())
						.subscribeOn(Schedulers.parallel()))
		            .expectSubscription()
		            .expectNextCount(5)
		            .verifyComplete();


		org.assertj.core.api.Assertions.assertThat(threads[0])
				.isNotEqualTo(originThreads[0]);
	}
}