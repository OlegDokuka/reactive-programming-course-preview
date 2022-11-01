import reactor.core.publisher.Flux;

import java.time.Duration;

public class SchedulePeriodicTask {

	public static Void executeTaskPeriodically(Runnable task, long periodInMillis) {
		for (;;) {
			task.run();
			try {
				Thread.sleep(periodInMillis);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}