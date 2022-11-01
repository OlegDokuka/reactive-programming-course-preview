import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;
import reactor.core.publisher.UnicastProcessor;
import reactor.test.StepVerifier;

public class CPTask5Tests {

	@Test
	public void ensureItWorksInCaseOfConnectionOpenIssue() {
		UnicastProcessor<Integer> holder1 = UnicastProcessor.create();
		UnicastProcessor<Integer> holder2 = UnicastProcessor.create();
		DatabaseApi mockOracleDb = Mockito.mock(DatabaseApi.class);
		DatabaseApi mockFileDb = Mockito.mock(DatabaseApi.class);
		DatabasesIntegration integration =
				new DatabasesIntegration(mockOracleDb, mockFileDb);
		TestConnection testConnection1 = new TestConnection();
		TestConnection testConnection2 = new TestConnection();

		testConnection1.dataHandler =
				(f) -> f.concatMap(i -> Mono.delay(Duration.ofMillis(1))
				                            .thenReturn(i))
				        .doAfterTerminate(() -> testConnection1.writeDone.onNext(1L))
				        .subscribe(holder1);
		testConnection2.dataHandler =
				(f) -> f.doAfterTerminate(() -> testConnection2.writeDone.onNext(2L))
				        .subscribe(holder2);

		Mockito.when(mockFileDb.<Integer>open())
		       .thenReturn(Mono.defer(new Supplier<Mono<? extends Connection<Integer>>>() {
			       boolean once = false;

			       @Override
			       public Mono<? extends Connection<Integer>> get() {
			       	if(!once) {
			       		once = true;
				        return Mono.error(IllegalAccessError::new);
			        }

			       	return Mono.just(testConnection1);
			       }
		       }));
		Mockito.when(mockOracleDb.<Integer>open())
		       .thenReturn(Mono.just(testConnection2));

		Mockito.when(mockFileDb.rollbackTransaction(Mockito.anyLong()))
		       .thenReturn(Mono.empty());
		Mockito.when(mockOracleDb.rollbackTransaction(Mockito.anyLong()))
		       .thenReturn(Mono.empty());

		StepVerifier.create(integration.storeToDatabases(Flux.range(0, 102)
		                                                     .subscribeWith(
				                                                     UnicastProcessor.create())))
		            .expectSubscription()
		            .expectComplete()
		            .verify(Duration.ofMillis(10000));

		Assertions.assertThat(holder1.isTerminated())
		          .isTrue();
		Assertions.assertThat(holder2.isTerminated())
		          .isTrue();

		StepVerifier.create(holder1)
		            .expectNext(0)
		            .expectNextCount(100)
		            .expectNext(101)
		            .verifyComplete();

		StepVerifier.create(holder2)
		            .expectNext(0)
		            .expectNextCount(100)
		            .expectNext(101)
		            .verifyComplete();
	}

	@Test
	public void ensureItRollbacksEverythingInCaseOfErrorInOne()
			throws InterruptedException {
		UnicastProcessor<Integer> holder1 = UnicastProcessor.create();
		UnicastProcessor<Integer> holder2 = UnicastProcessor.create();
		DatabaseApi mockOracleDb = Mockito.mock(DatabaseApi.class);
		DatabaseApi mockFileDb = Mockito.mock(DatabaseApi.class);
		DatabasesIntegration integration =
				new DatabasesIntegration(mockOracleDb, mockFileDb);
		TestConnection testConnection1 = new TestConnection();
		TestConnection testConnection2 = new TestConnection();

		testConnection1.dataHandler =
				(f) -> f.concatMap(i -> Mono.delay(Duration.ofMillis(1))
				                            .thenReturn(i))
				        .doAfterTerminate(() -> testConnection1.writeDone.onError(new RuntimeException()))
				        .subscribe(holder1);
		testConnection2.dataHandler =
				(f) -> f.doAfterTerminate(() -> testConnection2.writeDone.onNext(1L))
				        .subscribe(holder2);

		Mockito.when(mockFileDb.<Integer>open())
		       .thenReturn(Mono.just(testConnection1));
		Mockito.when(mockOracleDb.<Integer>open())
		       .thenReturn(Mono.just(testConnection2));

		Mockito.when(mockOracleDb.rollbackTransaction(Mockito.eq(1L)))
		       .thenReturn(Mono.empty());

		StepVerifier.create(integration.storeToDatabases(Flux.range(0, 100)
		                                                     .subscribeWith(
				                                                     UnicastProcessor.create())))
		            .expectSubscription()
		            .verifyError();

		Thread.sleep(1000);

		Assertions.assertThat(testConnection1.rolledBack)
		          .isTrue();
		Mockito.verify(mockOracleDb)
		       .rollbackTransaction(Mockito.eq(1L));
	}

	@Test
	public void checkTimeoutWorked() throws InterruptedException {
		UnicastProcessor<Integer> holder1 = UnicastProcessor.create();
		UnicastProcessor<Integer> holder2 = UnicastProcessor.create();
		DatabaseApi mockOracleDb = Mockito.mock(DatabaseApi.class);
		DatabaseApi mockFileDb = Mockito.mock(DatabaseApi.class);
		DatabasesIntegration integration =
				new DatabasesIntegration(mockOracleDb, mockFileDb);
		TestConnection testConnection1 = new TestConnection();
		TestConnection testConnection2 = new TestConnection();

		testConnection1.dataHandler =
				(f) -> f.concatMap(i -> Mono.delay(Duration.ofMillis(1))
				                            .thenReturn(i))
				        .doAfterTerminate(() -> testConnection1.writeDone.onError(new RuntimeException()))
				        .subscribe(holder1);
		testConnection2.dataHandler =
				(f) -> f.concatMap(i -> Mono.delay(Duration.ofMillis(1))
				                            .thenReturn(i))
				        .doAfterTerminate(() -> testConnection1.writeDone.onError(new RuntimeException()))
				        .subscribe(holder2);

		Mockito.when(mockFileDb.<Integer>open())
		       .thenReturn(Mono.just(testConnection1));
		Mockito.when(mockOracleDb.<Integer>open())
		       .thenReturn(Mono.just(testConnection2));

		StepVerifier.create(integration.storeToDatabases(Flux.range(0, 1000)
		                                                     .subscribeWith(
				                                                     UnicastProcessor.create())))
		            .expectSubscription()
		            .verifyError();

		Thread.sleep(1000);

		Assertions.assertThat(testConnection1.rolledBack)
		          .isTrue();
		Assertions.assertThat(testConnection2.rolledBack)
		          .isTrue();
	}

	class TestConnection implements Connection<Integer> {

		Consumer<Flux<Integer>> dataHandler;
		final    MonoProcessor<Long> writeDone = MonoProcessor.create();
		volatile boolean             rolledBack;
		volatile boolean             closed;

		@Override
		public Mono<Long> write(Flux<Integer> data) {
			dataHandler.accept(data);
			return writeDone;
		}

		@Override
		public Mono<Void> rollback() {
			rolledBack = true;
			return Mono.empty();
		}

		@Override
		public Mono<Void> close() {
			closed = true;
			return Mono.empty();
		}
	}
}