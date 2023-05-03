import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface KafkaClient {

	/**
	 * Retrieve stream of messages from specified topic
	 * @param topic name
	 * @return Flux of messages
	 */
	Flux<Object> receive(String topic);

	/**
	 * Send stream of messages to specified target
	 *
	 * @param messages Flux of messages to send
	 * @param target name
	 * @return Mono promise which completes when sending operation is done
	 */
	Mono<Void> send(Flux<Object> messages, String target);
}
