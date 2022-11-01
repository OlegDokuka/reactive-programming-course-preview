import java.time.Duration;

import reactor.core.publisher.Mono;

public class ValidationTask {

	public static boolean validate(Duration duration) {
		return !duration.isNegative() && !duration.isZero();
	}
}