package service.impl;

import java.time.Duration;
import java.util.HashMap;
import java.util.function.Predicate;

import dto.MessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import service.CryptoService;

import static service.utils.MessageMapper.*;

public class PriceServiceTests {
    private final CryptoService
		    cryptoService = Mockito.mock(CryptoService.class);

    @BeforeEach
    public void setUp() {
        Mockito.when(cryptoService.eventsStream()).thenReturn(Flux.empty());
    }

    @Test
    public void verifyBuildingCurrentPriceEvents() {
        StepVerifier.create(
                new DefaultPriceService(cryptoService).selectOnlyPriceUpdateEvents(
                        Flux.just(
                                new HashMap<String, Object>() {{ put("Invalid", "A"); }},
                                new HashMap<String, Object>() {{ put(TYPE_KEY, "1"); }},
                                new HashMap<String, Object>() {{ put(TYPE_KEY, "5"); }},
                                new HashMap<String, Object>() {
                                    {
                                        put(TYPE_KEY, "5");
                                        put(PRICE_KEY, 0.1F);
                                        put(CURRENCY_KEY, "USD");
                                        put(MARKET_KEY, "External");
                                    }
                                }
                        )
                )
        )
                .expectNext(
                        new HashMap<String, Object>() {
                            {
                                put(TYPE_KEY, "5");

                                put(PRICE_KEY, 0.1F);
                                put(CURRENCY_KEY, "USD");
                                put(MARKET_KEY, "External");
                            }
                        }
                )
                .expectComplete()
                .verify(Duration.ofSeconds(2));
    }

    // HINT: This is for reference implementation, your implementation may produce different average values
    @Test
    public void verifyBuildingAveragePriceEvents() {
        try {
            System.out.println("Checking case when flatMap is out of switch");
            StepVerifier.withVirtualTime(() ->
                    new DefaultPriceService(cryptoService).averagePrice(
                            Flux.interval(Duration.ofSeconds(0), Duration.ofSeconds(5))
                                    .map(i -> i + 1)
                                    .doOnNext(i -> System.out.println("Interval: " + i)),
                            Flux.interval(Duration.ofMillis(500), Duration.ofSeconds(1))
                                    .map(p -> p + 100)
                                    .map(tick -> MessageDTO.price((float) tick, "U", "M"))
                                    .take(20)
                                    .doOnNext(p -> System.out.println("Price: " + p.getData()))
                                    .replay(1000)
                                    .autoConnect()
                            )
                                                   .take(10)
                                                   .take(Duration.ofHours(1))
                                                   .map(MessageDTO::getData)
                                                   .doOnNext(a -> System.out.println("AVG: " + a))
            )
                    .expectSubscription()
                    .thenAwait(Duration.ofDays(1))
                    .expectNextMatches(expectedPrice(100.0F))
                    .expectNextMatches(expectedPrice(101.0F))
                    .expectNextMatches(expectedPrice(102.0F))
                    .expectNextMatches(expectedPrice(103.0F))
                    .expectNextMatches(expectedPrice(104.0F))
                    .expectNextMatches(expectedPrice(103.0F))
                    .expectNextMatches(expectedPrice(107.5F))
                    .expectNextMatches(expectedPrice(109.5F))
                    .expectNextMatches(expectedPrice(106.0F))
                    .expectNextMatches(expectedPrice(114.0F))
                    .verifyComplete();
        } catch (Throwable e) {
            System.out.println("Checking case when flatMap is inside the switch");
            StepVerifier.withVirtualTime(() ->
                    new DefaultPriceService(cryptoService).averagePrice(
                            Flux.interval(Duration.ofSeconds(0), Duration.ofSeconds(5))
                                .map(i -> i + 1)
                                .doOnNext(i -> System.out.println("Interval: " + i)),
                            Flux.interval(Duration.ofMillis(500), Duration.ofSeconds(1))
                                .map(p -> p + 100)
                                .map(tick -> MessageDTO.price((float) tick, "U", "M"))
                                .take(20)
                                .doOnNext(p -> System.out.println("Price: " + p.getData()))
                                .replay(1000)
                                .autoConnect()
                    )
                                                          .take(10)
                                                          .take(Duration.ofHours(1))
                                                          .map(MessageDTO::getData)
                                                          .doOnNext(a -> System.out.println("AVG: " + a))
            )
                        .expectSubscription()
                        .thenAwait(Duration.ofDays(1))
                        .expectNextMatches(expectedPrice(100.0F))
                        .expectNextMatches(expectedPrice(101.0F))
                        .expectNextMatches(expectedPrice(102.0F))
                        .expectNextMatches(expectedPrice(103.0F))
                        .expectNextMatches(expectedPrice(103.0F))
                        .expectNextMatches(expectedPrice(107.5F))
                        .expectNextMatches(expectedPrice(106.0F))
                        .expectNextMatches(expectedPrice(109.0F))
                        .expectNextMatches(expectedPrice(119.0F))
                        .expectNextMatches(expectedPrice(109.5F))
                        .verifyComplete();
        }
    }

    private Predicate<Float> expectedPrice(float v) {
        return m -> (Math.abs(m - v) < 0.0001);
    }
}