package service.external;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;
import service.CryptoService;
import service.external.utils.PriceMessageUnpacker;
import service.external.utils.TradeMessageUnpacker;

//TODO turn to multi-subscriber with processor or another similar operator
//TODO add small history for each subscriber
//TODO add resilience

public class CryptoCompareService implements CryptoService {
    public static final int CACHE_SIZE = 3;

    private final Flux<Map<String, Object>> reactiveCryptoListener;

    public CryptoCompareService() {
        reactiveCryptoListener = CryptoCompareClient
                .connect(
                        Flux.just("5~CCCAGG~BTC~USD", "0~Coinbase~BTC~USD", "0~Cexio~BTC~USD"),
                        Arrays.asList(new PriceMessageUnpacker(), new TradeMessageUnpacker())
                )
                .transform(CryptoCompareService::provideResilience)
                .transform(CryptoCompareService::provideCaching);
    }

    public Flux<Map<String, Object>> eventsStream() {
        return reactiveCryptoListener;
    }

    // TODO: implement resilience such as retry with delay
    public static <T> Flux<T> provideResilience(Flux<T> input) {
        return Flux.never();
    }


    // TODO: implement caching of 3 last elements & multi subscribers support
    public static <T> Flux<T> provideCaching(Flux<T> input) {
        return Flux.never();
    }
}
