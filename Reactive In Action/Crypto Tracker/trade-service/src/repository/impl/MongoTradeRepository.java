package repository.impl;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.Success;
import domain.Trade;
import domain.utils.DomainMapper;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import repository.TradeRepository;

public class MongoTradeRepository implements TradeRepository {

	private static final Logger log = LoggerFactory.getLogger("mongo-repo");

	private static final String DB_NAME         = "crypto";
	private static final String COLLECTION_NAME = "trades";

	private final MongoCollection<Document> collection;

	public MongoTradeRepository(MongoClient client) {
		collection = client.getDatabase(DB_NAME)
		                   .getCollection(COLLECTION_NAME);

		reportDbStatistics();
	}


	private void reportDbStatistics() {
		Flux.interval(Duration.ofSeconds(5))
		    .flatMap(i -> this.getTradeStats())
		    .doOnNext(count -> log.warn("------------- [DB STATS] ------------ Trades " +
				    "stored in DB: " + count))
		    .subscribeOn(Schedulers.elastic())
		    .subscribe();
	}

	private Mono<Long> getTradeStats() {
		// TODO: Return the current amount of stored trades
		return Mono.fromDirect(collection.countDocuments());
	}

	public Mono<Void> saveAll(List<Trade> trades) {
		return this
			.storeInMongo(
				trades
					.stream()
					.map(DomainMapper::mapToMongoDocument)
					.collect(Collectors.toList())
			)
			.then();
	}

	private Mono<Success> storeInMongo(List<Document> trades) {
		return Mono.from(collection.insertMany(trades));
	}
}
