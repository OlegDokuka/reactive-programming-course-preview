import reactor.core.publisher.Mono;

public class LoadbalancedMessageProcessorTask {

	static KafkaLoadbalancer kafkaLoadbalancer;
	static KafkaClient       kafkaClient;
	static MessageProcessor processor;

	public static Mono<Void> balanceTrafficFrom(String receiveTopic) {
		return kafkaClient.receive(receiveTopic)				
                   .transform(processor::process)			
                   .as(f -> kafkaClient.send(f, "FIXME"))
                   .then();
	}
}