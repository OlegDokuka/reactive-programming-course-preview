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
		return sharedStream.publish(mainFlow -> Flux.merge(
			mainFlow,
			averagePrice(intervalPreferencesStream, mainFlow)
		));
	}

	// FIXME:
	// 1) JUST FOR WARM UP: .map() incoming Map<String, Object> to MessageDTO. For that purpose use MessageDTO.price()
	//    NOTE: Incoming Map<String, Object> contains keys PRICE_KEY and CURRENCY_KEY
	//    NOTE: Use MessageMapper utility class for message validation and transformation
	// Visible for testing
	Flux<Map<String, Object>> selectOnlyPriceUpdateEvents(Flux<Map<String, Object>> input) {
		// TODO: filter only Price messages
		// TODO: verify that price message are valid
		// HINT: Use MessageMapper methods to perform filtering and validation

		return Flux.never();
	}

	// Visible for testing
	Flux<MessageDTO<Float>> tranformToPriceMessageDTO(Flux<Map<String, Object>> input) {
		// TODO map to Statistic message using MessageMapper.mapToPriceMessage

		return Flux.never();
	}

	// 1.1)   TODO Collect crypto currency price during the interval of seconds
	//        HINT consider corner case when a client did not send any info about interval (add initial interval (mergeWith(...)))
	//        HINT use window + switchMap
	// 1.2)   TODO group collected MessageDTO results by currency
	//        HINT for reduce consider to reuse Sum.empty and Sum#add
	// 1.3.2) TODO calculate average for reduced Sum object using Sun#avg
	// 1.3.3) TODO map to Statistic message using MessageDTO#avg()

	// Visible for testing
	// TODO: Remove as should be implemented by trainees
	Flux<MessageDTO<Float>> averagePrice(Flux<Long> requestedInterval,
			Flux<MessageDTO<Float>> priceData) {

		return Flux.never();
	}
}
