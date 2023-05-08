import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;
import reactor.util.function.Tuple2;

public class LogsObserverTests {

	@Test
	public void mergeSeveralSourcesTest() {
		List<String> logsList = Arrays.asList(
				"[15:25:54.962] |-INFO in ch.qos.logback.classic.LoggerContext[default] - Could NOT find resource [logback-test.xml]",
				"[15:25:54.963] |-INFO in ch.qos.logback.classic.LoggerContext[default] - Found resource [logback.xml] at [file:/Users/olehdokuka/Documents/Workspace/Java/Reactive/reactor-core/reactor-loom/build/resources/test/logback.xml]",
				"[15:25:54.964] |-WARN in ch.qos.logback.classic.LoggerContext[default] - Resource [logback.xml] occurs multiple times on the classpath.",
				"[15:25:55.073] |-INFO in ch.qos.logback.classic.joran.action.ConfigurationAction - debug attribute not set",
				"[15:25:55.074] |-INFO in ch.qos.logback.core.joran.action.AppenderAction - About to instantiate appender of type [ch.qos.logback.core.ConsoleAppender]",
				"[15:25:55.087] |-INFO in ch.qos.logback.core.joran.action.AppenderAction - Naming appender as [stdout]",
				"[15:25:55.095] |-INFO in ch.qos.logback.core.joran.action.NestedComplexPropertyIA - Assuming default type [ch.qos.logback.classic.encoder.PatternLayoutEncoder] for [encoder] property",
				"[15:25:55.145] |-INFO in ch.qos.logback.classic.joran.JoranConfigurator@4bd2f0dc - Registering current configuration as safe fallback point",
				"[15:25:55.711] [Test worker] INFO  before - | onSubscribe([Synchronous Fuseable] FluxRange.RangeSubscription)",
				"[15:25:55.718] [Test worker] INFO  after - onSubscribe(FluxFlatMap.FlatMapMain)",
				"[15:25:55.719] [Test worker] INFO  before - | request(256)",
				"[15:25:55.729] [Test worker] INFO  before - | onNext(0)",
				"[15:25:55.793] [Test worker] INFO  before - | onNext(3)",
				"[15:25:55.794] [Test worker] INFO  before - | onNext(4)",
				"[15:25:55.795] [Test worker] INFO  before - | onNext(7)",
				"[15:25:55.796] [Test worker] INFO  before - | onNext(10)",
				"[15:25:55.797] [Test worker] INFO  before - | onNext(14)",
				"[15:25:55.798] [Test worker] INFO  before - | onNext(16)",
				"[15:25:55.799] [Test worker] INFO  before - | onNext(18)",
				"[15:25:55.800] [Test worker] INFO  before - | onNext(19)",
				"[15:25:55.801] [Test worker] INFO  before - | onNext(21)",
				"[15:25:55.802] [Test worker] INFO  before - | onNext(24)",
				"[15:25:55.803] [Test worker] INFO  before - | onNext(28)",
				"[15:25:55.804] [Test worker] INFO  before - | onNext(33)",
				"[15:25:55.805] [Test worker] INFO  before - | onNext(37)",
				"[15:25:55.806] [Test worker] INFO  before - | onNext(40)",
				"[15:25:55.807] [Test worker] INFO  before - | onNext(43)",
				"[15:25:55.808] [Test worker] INFO  before - | onNext(46)",
				"[15:25:55.809] [Test worker] INFO  before - | onNext(50)",
				"[15:25:55.810] [Test worker] INFO  before - | onNext(54)",
				"[15:25:55.811] [Test worker] INFO  before - | onNext(58)",
				"[15:25:55.812] [Test worker] INFO  before - | onNext(62)");

		DiscoveryService discoveryService = Mockito.mock(DiscoveryService.class);

		Server server1 = Mockito.mock(Server.class);
		Server server2 = Mockito.mock(Server.class);

		Mockito.when(discoveryService.activeServers())
		       .thenReturn(Mono.just(Arrays.asList(server1, server2)));

		Mockito.when(server1.logsStream())
		       .thenReturn(Flux.fromIterable(logsList)
		                       .index()
		                       .filter(t -> t.getT1() % 2 == 0)
		                       .map(Tuple2::getT2));
		Mockito.when(server2.logsStream())
		       .thenReturn(Flux.fromIterable(logsList)
		                       .index()
		                       .filter(t -> t.getT1() % 2 == 1)
		                       .map(Tuple2::getT2));

		LogsObserverTask.discoveryService = discoveryService;

		StepVerifier.create(LogsObserverTask.observeLogs())
		            .expectSubscription()
		            .recordWith(ArrayList::new)
		            .expectNextCount(32)
		            .consumeRecordedWith(recorded -> Assertions.assertThat(recorded)
		                                                       .isEqualTo(logsList))
		            .verifyComplete();
	}

	@Test
	public void mergeSeveralSourcesTest2() {
		List<String> logsList = Arrays.asList(
				"[15:25:54.962] |-INFO in ch.qos.logback.classic.LoggerContext[default] - Could NOT find resource [logback-test.xml]",
				"[15:25:54.963] |-INFO in ch.qos.logback.classic.LoggerContext[default] - Found resource [logback.xml] at [file:/Users/olehdokuka/Documents/Workspace/Java/Reactive/reactor-core/reactor-loom/build/resources/test/logback.xml]",
				"[15:25:54.964] |-WARN in ch.qos.logback.classic.LoggerContext[default] - Resource [logback.xml] occurs multiple times on the classpath.",
				"[15:25:55.073] |-INFO in ch.qos.logback.classic.joran.action.ConfigurationAction - debug attribute not set",
				"[15:25:55.074] |-INFO in ch.qos.logback.core.joran.action.AppenderAction - About to instantiate appender of type [ch.qos.logback.core.ConsoleAppender]",
				"[15:25:55.087] |-INFO in ch.qos.logback.core.joran.action.AppenderAction - Naming appender as [stdout]",
				"[15:25:55.095] |-INFO in ch.qos.logback.core.joran.action.NestedComplexPropertyIA - Assuming default type [ch.qos.logback.classic.encoder.PatternLayoutEncoder] for [encoder] property",
				"[15:25:55.145] |-INFO in ch.qos.logback.classic.joran.JoranConfigurator@4bd2f0dc - Registering current configuration as safe fallback point",
				"[15:25:55.711] [Test worker] INFO  before - | onSubscribe([Synchronous Fuseable] FluxRange.RangeSubscription)",
				"[15:25:55.718] [Test worker] INFO  after - onSubscribe(FluxFlatMap.FlatMapMain)",
				"[15:25:55.719] [Test worker] INFO  before - | request(256)",
				"[15:25:55.729] [Test worker] INFO  before - | onNext(0)",
				"[15:25:55.793] [Test worker] INFO  before - | onNext(3)",
				"[15:25:55.794] [Test worker] INFO  before - | onNext(4)",
				"[15:25:55.795] [Test worker] INFO  before - | onNext(7)",
				"[15:25:55.796] [Test worker] INFO  before - | onNext(10)",
				"[15:25:55.797] [Test worker] INFO  before - | onNext(14)",
				"[15:25:55.798] [Test worker] INFO  before - | onNext(16)",
				"[15:25:55.799] [Test worker] INFO  before - | onNext(18)",
				"[15:25:55.800] [Test worker] INFO  before - | onNext(19)",
				"[15:25:55.801] [Test worker] INFO  before - | onNext(21)",
				"[15:25:55.802] [Test worker] INFO  before - | onNext(24)",
				"[15:25:55.803] [Test worker] INFO  before - | onNext(28)",
				"[15:25:55.804] [Test worker] INFO  before - | onNext(33)",
				"[15:25:55.805] [Test worker] INFO  before - | onNext(37)",
				"[15:25:55.806] [Test worker] INFO  before - | onNext(40)",
				"[15:25:55.807] [Test worker] INFO  before - | onNext(43)",
				"[15:25:55.808] [Test worker] INFO  before - | onNext(46)",
				"[15:25:55.809] [Test worker] INFO  before - | onNext(50)",
				"[15:25:55.810] [Test worker] INFO  before - | onNext(54)",
				"[15:25:55.811] [Test worker] INFO  before - | onNext(58)",
				"[15:25:55.812] [Test worker] INFO  before - | onNext(62)");

		DiscoveryService discoveryService = Mockito.mock(DiscoveryService.class);

		Server server1 = Mockito.mock(Server.class);
		Server server2 = Mockito.mock(Server.class);

		Mockito.when(discoveryService.activeServers())
		       .thenReturn(Mono.just(Arrays.asList(server1, server2)));

		TestPublisher<String> publisher1 = TestPublisher.create();
		TestPublisher<String> publisher2 = TestPublisher.create();

		Mockito.when(server1.logsStream())
		       .thenReturn(publisher1.flux());
		Mockito.when(server2.logsStream())
		       .thenReturn(publisher2.flux());

		LogsObserverTask.discoveryService = discoveryService;

		StepVerifier.create(LogsObserverTask.observeLogs())
		            .expectSubscription()

		            .then(() -> publisher1.next(
				            "[15:25:54.962] |-INFO in ch.qos.logback.classic.LoggerContext[default] - Could NOT find resource [logback-test.xml]"))
		            .expectNoEvent(Duration.ofMillis(100))
		            .then(() -> publisher2.next(
				            "[15:25:54.963] |-INFO in ch.qos.logback.classic.LoggerContext[default] - Found resource [logback.xml] at [file:/Users/olehdokuka/Documents/Workspace/Java/Reactive/reactor-core/reactor-loom/build/resources/test/logback.xml]"))
		            .expectNoEvent(Duration.ofMillis(100))
					.then(() -> publisher1.next(
							"[15:25:54.964] |-WARN in ch.qos.logback.classic.LoggerContext[default] - Resource [logback.xml] occurs multiple times on the classpath."))

		            .expectNext(
				            "[15:25:54.962] |-INFO in ch.qos.logback.classic.LoggerContext[default] - Could NOT find resource [logback-test.xml]")
		            .expectNext(
				            "[15:25:54.963] |-INFO in ch.qos.logback.classic.LoggerContext[default] - Found resource [logback.xml] at [file:/Users/olehdokuka/Documents/Workspace/Java/Reactive/reactor-core/reactor-loom/build/resources/test/logback.xml]")

		            .then(() -> publisher2.next(
				            "[15:25:55.729] [Test worker] INFO  before - | onNext(0)"))

		            .expectNext(
				            "[15:25:54.964] |-WARN in ch.qos.logback.classic.LoggerContext[default] - Resource [logback.xml] occurs multiple times on the classpath.")

		            .then(() -> publisher2.next(
				            "[15:25:55.794] [Test worker] INFO  before - | onNext(4)"))
		            .then(() -> publisher2.next(
				            "[15:25:55.795] [Test worker] INFO  before - | onNext(7)"))
		            .expectNoEvent(Duration.ofMillis(100))
		            .then(() -> publisher2.next(
				"[15:25:55.796] [Test worker] INFO  before - | onNext(10)"))
		            .then(() -> publisher1.next(
				            "[15:25:55.807] [Test worker] INFO  before - | onNext(43)"))

		            .expectNext(
				            "[15:25:55.729] [Test worker] INFO  before - | onNext(0)")
		            .expectNext(
				            "[15:25:55.794] [Test worker] INFO  before - | onNext(4)")
		            .expectNext(
				            "[15:25:55.795] [Test worker] INFO  before - | onNext(7)")

					.thenCancel()
		            .verify();
	}
}