import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestingTask2Tests {

	@Test
	public void testSolution() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		new Thread(() -> {
			Task.verifyEmissionWithVirtualTimeScheduler();
			latch.countDown();
		}).start();

		Assertions.assertThat(latch.await(10, TimeUnit.SECONDS))
		          .isTrue();
	}
}