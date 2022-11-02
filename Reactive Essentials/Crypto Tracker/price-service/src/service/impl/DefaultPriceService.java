package service.impl;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Level;

import dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.CryptoService;
import service.PriceService;
import service.utils.MessageMapper;
import service.utils.Sum;

public class DefaultPriceService implements PriceService {

	private static final Logger logger = LoggerFactory.getLogger("price-service");

	private static final long DEFAULT_AVG_PRICE_INTERVAL = 30L;

	private final Flux<MessageDTO<Float>> sharedStream;

	public DefaultPriceService(CryptoService cryptoService) {
		sharedStream = cryptoService.eventsStream()
		                            .log("Incoming event: {}", Level.INFO)
		                            .transform(this::selectOnlyPriceUpdateEvents)
		                            .transform(this::tranformToPriceMessageDTO)
		                            .log("Price event: {}", Level.INFO);
	}

	public Flux<MessageDTO<Float>> pricesStream(Flux<Long> intervalPreferencesStream) {
		return Flux.merge(
			sharedStream,
			averagePrice(intervalPreferencesStream, sharedStream)
		);
	}

	Flux<Map<String, Object>> selectOnlyPriceUpdateEvents(Flux<Map<String, Object>> input) {
		/* TODO: add filtering */ 
return Flux.never();
	}

	Flux<MessageDTO<Float>> tranformToPriceMessageDTO(Flux<Map<String, Object>> input) {
		/* TODO: add mapping to price message. Use MessageMpper */ 
return Flux.never();
	}

	Flux<MessageDTO<Float>> averagePrice(Flux<Long> requestedInterval,
			Flux<MessageDTO<Float>> priceData) {

		return /* TODO: calcualte average price.  Use window(Duration) + groupBy */

	return Flux.never();
}
}
