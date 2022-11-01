import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DelayExecutionTask {

	public static Void pauseExecution(long pauseDurationInMillis) {
		try {
			Thread.sleep(pauseDurationInMillis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
}