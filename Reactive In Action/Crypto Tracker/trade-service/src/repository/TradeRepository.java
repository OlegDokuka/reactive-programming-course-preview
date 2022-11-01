package repository;

import java.util.List;

import domain.Trade;
import reactor.core.publisher.Mono;

public interface TradeRepository {

	Mono<Void> saveAll(List<Trade> input);
}
