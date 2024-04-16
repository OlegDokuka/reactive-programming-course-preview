import java.math.BigDecimal;
import java.util.Map;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyRepository {

	Mono<Map<String, BigDecimal>> currencyRates();

	Flux<Map<String, BigDecimal>> currenciesRatesChanges();
}
