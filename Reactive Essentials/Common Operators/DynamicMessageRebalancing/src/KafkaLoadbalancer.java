import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface KafkaLoadbalancer {

	/**
	 * retrieves current active target to which we have to send messages
	 * @return mono of current target to send
	 */
	Mono<String> activeTarget();

	/**
	 * stream of new active target updates. given flux emits new value every time the
	 * active target has changed
	 * @return flux of new targets to send messages
	 */
	Flux<String> targetChangeNotificationsStream();

}
