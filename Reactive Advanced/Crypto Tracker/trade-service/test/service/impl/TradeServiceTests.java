package service.impl;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import domain.Trade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;
import reactor.test.scheduler.VirtualTimeScheduler;
import repository.TradeRepository;
import repository.impl.MongoTradeRepository;
import service.CryptoService;

public class TradeServiceTests {

	@Test
	public void tradeServiceTest() {
		VirtualTimeScheduler timeScheduler = VirtualTimeScheduler.getOrSet();
		try {
			// TODO add more tests
			CryptoService cryptoService = Mockito.mock(CryptoService.class);
			TradeRepository h2Repository = Mockito.mock(TradeRepository.class);
			MongoTradeRepository mongoRepository = Mockito.mock(MongoTradeRepository.class);

			Mockito.when(cryptoService.eventsStream())
			       .thenReturn(Flux.never());
			DefaultTradeService tradeService =
					new DefaultTradeService(cryptoService, h2Repository, mongoRepository);
			TestPublisher<Trade> testPublisher = TestPublisher.create();

			LinkedList<List> batchesH2Queue = new LinkedList<>();
			LinkedList<List> batchesMongoQueue = new LinkedList<>();

			TestPublisher publisher = TestPublisher.create();


			Mockito.when(h2Repository.saveAll(Mockito.any()))
			       .thenAnswer(a -> {
				       List<Trade> list = a.getArgument(0);
				       batchesH2Queue.add(list);
				       return Mono.empty();
			       });

			Mockito.when(mongoRepository.saveAll(Mockito.any()))
			       .thenAnswer(a -> {
				       List<Trade> list = a.getArgument(0);
				       batchesMongoQueue.add(list);
				       return publisher.flux().next().then();
			       });

			Mono<Void> result = tradeService.resilientlyStoreByBatchesToAllRepositories(
					testPublisher.flux(),
					h2Repository,
					mongoRepository);

			StepVerifier.withVirtualTime(() -> result)
			            .expectSubscription()
			            .then(() -> {
				            testPublisher.next(createTrade("1"));
				            testPublisher.next(createTrade("2"));
				            testPublisher.next(createTrade("3"));
				            testPublisher.next(createTrade("4"));

				            timeScheduler.advanceTimeBy(Duration.ofSeconds(1));

				            publisher.next(1);
				            Assertions.assertThat(batchesH2Queue)
				                      .containsExactly(Arrays.asList(
					                      createTrade("1"),
					                      createTrade("2"),
					                      createTrade("3"),
					                      createTrade("4")
				                      ));
				            Assertions.assertThat(batchesMongoQueue)
				                      .containsExactly(Arrays.asList(
						                      createTrade("1"),
						                      createTrade("2"),
						                      createTrade("3"),
						                      createTrade("4")
				                      ));

				            batchesMongoQueue.clear();
				            batchesH2Queue.clear();
			            })
			            .then(() -> {
				            testPublisher.next(createTrade("1"));
				            testPublisher.next(createTrade("2"));
				            testPublisher.next(createTrade("3"));
				            testPublisher.next(createTrade("4"));

				            timeScheduler.advanceTimeBy(Duration.ofSeconds(1));


				            testPublisher.next(createTrade("5"));
				            testPublisher.next(createTrade("6"));
				            testPublisher.next(createTrade("7"));
				            testPublisher.next(createTrade("8"));

//				            publisher.next(1);
				            Assertions.assertThat(batchesH2Queue)
				                      .containsExactly(Arrays.asList(
						                      createTrade("1"),
						                      createTrade("2"),
						                      createTrade("3"),
						                      createTrade("4")
				                      ));
				            Assertions.assertThat(batchesMongoQueue)
				                      .containsExactly(Arrays.asList(
						                      createTrade("1"),
						                      createTrade("2"),
						                      createTrade("3"),
						                      createTrade("4")
				                      ));

				            // timeout
				            timeScheduler.advanceTimeBy(Duration.ofSeconds(1));
				            // retryBackoff
				            timeScheduler.advanceTimeBy(Duration.ofSeconds(1));



				            testPublisher.next(createTrade("9"));
				            testPublisher.next(createTrade("10"));
				            testPublisher.next(createTrade("11"));
				            testPublisher.next(createTrade("12"));


				            Assertions.assertThat(publisher.subscribeCount())
				                      .isEqualTo(3);

				            batchesH2Queue.clear();
				            batchesMongoQueue.clear();
				            publisher.next(1);

				            timeScheduler.advanceTimeBy(Duration.ofSeconds(1));

				            Assertions.assertThat(batchesH2Queue)
				                      .containsExactly(Arrays.asList(
						                      createTrade("5"),
						                      createTrade("6"),
						                      createTrade("7"),
						                      createTrade("8"),
						                      createTrade("9"),
						                      createTrade("10"),
						                      createTrade("11"),
						                      createTrade("12")
				                      ));
				            Assertions.assertThat(batchesMongoQueue)
				                      .containsExactly(Arrays.asList(
						                      createTrade("5"),
						                      createTrade("6"),
						                      createTrade("7"),
						                      createTrade("8"),
						                      createTrade("9"),
						                      createTrade("10"),
						                      createTrade("11"),
						                      createTrade("12")
				                      ));
			            })
			            .thenCancel()
			            .verify();

		} finally {
			VirtualTimeScheduler.reset();
		}
	}

	public static Trade createTrade(String id) {
		Trade trade = new Trade();
		trade.setId(id);
		trade.setCurrency("test");
		trade.setAmount(1);
		trade.setMarket("test");
		trade.setPrice(1);
		trade.setTimestamp(1);
		return trade;
	}
}