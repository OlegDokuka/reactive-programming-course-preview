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

	Flux<Map<String, Object>> selectOnlyPriceUpdateEvents(Flux<Map<String, Object>> input) {
		return input.filter(m -> MessageMapper.isPriceMessageType(m) && MessageMapper.isValidPriceMessage(m));
	}

	// Visible for testing
	Flux<MessageDTO<Float>> tranformToPriceMessageDTO(Flux<Map<String, Object>> input) {
		return input.map(MessageMapper::mapToPriceMessage);
	}

	Flux<MessageDTO<Float>> averagePrice(Flux<Long> requestedInterval,
			Flux<MessageDTO<Float>> priceData) {

		return requestedInterval
				.startWith(DEFAULT_AVG_PRICE_INTERVAL)
				.switchMap(timeFrame ->
					priceData.window(Duration.ofSeconds(timeFrame))
					         .flatMap(this::currencyGroupingLogic)
				);


	}

	private Flux<MessageDTO<Float>> currencyGroupingLogic(Flux<MessageDTO<Float>> flux) {
        return flux.groupBy(MessageDTO::getCurrency)
            .flatMap(gf -> averageProcessingLogic(gf, gf.key()));
    }

	private Mono<MessageDTO<Float>> averageProcessingLogic(Flux<MessageDTO<Float>> flux,
            String currency) {
		return flux.map(MessageDTO::getData)
				   .reduce(Sum.empty(), Sum::add)
				   .map(Sum::avg)
				   .map(avg -> MessageDTO.avg(avg, currency, "LocalMarketAvg"));
	}
}
