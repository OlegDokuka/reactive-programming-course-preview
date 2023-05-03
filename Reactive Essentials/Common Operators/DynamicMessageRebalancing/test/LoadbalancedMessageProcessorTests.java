import java.time.Duration;
import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;
import reactor.test.publisher.TestPublisher;

public class LoadbalancedMessageProcessorTests {
	@Test
	public void testSolution() {
		KafkaLoadbalancer kafkaLoadbalancer = Mockito.mock(KafkaLoadbalancer.class);
		KafkaClient kafkaClient = Mockito.mock(KafkaClient.class);
		MessageProcessor processor = Mockito.mock(MessageProcessor.class);

		LoadbalancedMessageProcessorTask.kafkaLoadbalancer = kafkaLoadbalancer;
		LoadbalancedMessageProcessorTask.kafkaClient = kafkaClient;
		LoadbalancedMessageProcessorTask.processor = processor;

		TestPublisher<String> targetsPublisher = TestPublisher.create();

		Mockito.when(kafkaLoadbalancer.activeTarget()).then((a) -> Mono.just("test1").delayElement(Duration.ofSeconds(1)));
		Mockito.when(kafkaLoadbalancer.targetChangeNotificationsStream()).thenReturn(targetsPublisher.flux());

		PublisherProbe<Long>[] sourceProbe = new PublisherProbe[1];

		Mockito.when(kafkaClient.receive(Mockito.anyString()))
		       .thenAnswer((a) -> {
			       sourceProbe[0] =
					       PublisherProbe.of(Flux.interval(Duration.ofMillis(100)));
			       String argument = a.getArgument(0);
			       return sourceProbe[0].flux()
			                            .map(i -> i + String.valueOf(argument));
		       });

		ArrayList<Object> observedMessages = new ArrayList<>();

		Mockito.when(kafkaClient.send(Mockito.any(), Mockito.anyString()))
		       .thenAnswer((a) -> {
			       String argument = a.getArgument(1);
			       return a.<Flux<Object>>getArgument(0).map(m -> m + argument).doOnNext(observedMessages::add).then();
		       });

		Mockito.when(processor.process(Mockito.any())).thenAnswer(a -> a.getArgument(0));

		StepVerifier.withVirtualTime(() -> LoadbalancedMessageProcessorTask.balanceTrafficFrom("test"))
				.expectSubscription()
				.then(() -> {
					Mockito.verify(kafkaLoadbalancer).activeTarget();
					Mockito.verifyNoInteractions(kafkaClient);
					Mockito.verifyNoInteractions(processor);
				})
				.thenAwait(Duration.ofSeconds(1))
				.then(() -> {
					ArgumentCaptor<Flux> sourceCaptor = ArgumentCaptor.forClass(Flux.class);
					ArgumentCaptor<String> targetCaptor =
							ArgumentCaptor.forClass(String.class);
					Mockito.verify(kafkaClient).send(sourceCaptor.capture(), targetCaptor.capture());
					Mockito.verify(processor, Mockito.times(1)).process(Mockito.any());
					Assertions.assertThat(targetCaptor.getValue()).isEqualTo("test1");
				})
			    .thenAwait(Duration.ofMillis(400))
				.then(() -> {
					targetsPublisher.next("test2");
					ArgumentCaptor<Flux> sourceCaptor = ArgumentCaptor.forClass(Flux.class);
					ArgumentCaptor<String> targetCaptor =
							ArgumentCaptor.forClass(String.class);
					Mockito.verify(kafkaClient, Mockito.times(2)).send(sourceCaptor.capture(),
							targetCaptor.capture());
					Mockito.verify(processor, Mockito.times(2)).process(Mockito.any());
					Assertions.assertThat(targetCaptor.getValue()).isEqualTo("test2");
				})
			    .thenAwait(Duration.ofMillis(200))
				.thenCancel()
				.verify();

		Assertions.assertThat(observedMessages).containsExactly("0testtest1",
				"1testtest1", "2testtest1", "3testtest1", "0testtest2", "1testtest2");
	}
}