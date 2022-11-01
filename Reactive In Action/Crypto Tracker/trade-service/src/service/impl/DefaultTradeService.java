package service.impl;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.mongodb.MongoException;
import domain.Trade;
import domain.utils.DomainMapper;
import dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.util.retry.Retry;
import repository.TradeRepository;
import service.CryptoService;
import service.TradeService;
import service.utils.MessageMapper;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

public class DefaultTradeService implements TradeService {

	private static final Logger logger = LoggerFactory.getLogger("trade-service");

	private final Flux<MessageDTO<MessageDTO.Trade>> sharedStream;

	public DefaultTradeService(CryptoService service,
			TradeRepository jdbcRepository,
			TradeRepository mongoRepository
	) {
		service.eventsStream()
		       .transform(this::filterAndMapTradingEvents)
		       .transform(this::mapToDomainTrade)
		       .as(f -> this.resilientlyStoreByBatchesToAllRepositories(f, jdbcRepository, mongoRepository))
		       .subscribe();
		sharedStream = service.eventsStream()
		                      .transform(this::filterAndMapTradingEvents);
	}

	@Override
	public Flux<MessageDTO<MessageDTO.Trade>> tradesStream() {
		return sharedStream;
	}

	Flux<MessageDTO<MessageDTO.Trade>> filterAndMapTradingEvents(Flux<Map<String, Object>> input) {
		// TODO: Add implementation to produce trading events
		return Flux.never();
	}

	Flux<Trade> mapToDomainTrade(Flux<MessageDTO<MessageDTO.Trade>> input) {
		// TODO: Add implementation to mapping to com.example.part_10.domain.Trade
		return Flux.never();
	}

	Mono<Void> resilientlyStoreByBatchesToAllRepositories(
			Flux<Trade> input,
			TradeRepository tradeRepository1,
			TradeRepository tradeRepository2) {
		return Mono.never();
	}

	Mono<Integer> saveIntoMongoDatabase(TradeRepository tradeRepository1, List<Trade> trades) {
		return Mono.never();
	}

	Mono<Integer> saveIntoRelationalDatabase(TradeRepository tradeRepository2, List<Trade> trades) {
		return Mono.never();
	}

}
