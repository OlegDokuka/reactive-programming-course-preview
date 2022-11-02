package service.external;

import reactor.core.publisher.Flux;
import service.CryptoService;
import service.external.utils.PriceMessageUnpacker;
import service.external.utils.TradeMessageUnpacker;

import java.util.Arrays;
import java.util.Map;

public class CryptoCompareService implements CryptoService {

    private final Flux<Map<String, Object>> reactiveCryptoListener;

    public CryptoCompareService() {
        reactiveCryptoListener = CryptoCompareClient
                .connect(
                        Arrays.asList("5~CCCAGG~BTC~USD", "0~Coinbase~BTC~USD", "0~Cexio~BTC~USD"),
                        Arrays.asList(new PriceMessageUnpacker(), new TradeMessageUnpacker())
                );
    }

    public Flux<Map<String, Object>> eventsStream() {
        return reactiveCryptoListener;
    }
}
