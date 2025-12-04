import java.io.File;
import java.time.Duration;

import jdk.nashorn.internal.ir.Block;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class NonBlockingTempFileTaskTests {

	static {
		BlockHound.install();
	}

	@Test
	public void testSolution() {
		Thread[] threads = new Thread[1];
		Thread[] originThreads = new Thread[1];
		Files mock = Mockito.mock(Files.class);
		Mockito.when(mock.createTempFile(Mockito.any(), Mockito.any())).thenAnswer(a -> {
			threads[0] = Thread.currentThread();
			return File.createTempFile(a.getArgument(0), a.getArgument(1));
		});
		NonBlockingTempFileTask.files = mock;
		Object file = NonBlockingTempFileTask.createTempFile("test", null);

		if (!(file instanceof Mono)) {
			((File) file).deleteOnExit();
		}

		StepVerifier.create(((Mono<File>) file)
						.doFirst(() -> originThreads[0] = Thread.currentThread())
						.subscribeOn(Schedulers.parallel()))
		            .expectNextMatches(f -> {
						boolean exists = f.exists();
						f.deleteOnExit();
						return exists;
					})
		            .expectComplete()
		            .verify(Duration.ofMillis(1000));


		Assertions.assertThat(threads[0])
				.isNotEqualTo(originThreads[0]);
	}
}