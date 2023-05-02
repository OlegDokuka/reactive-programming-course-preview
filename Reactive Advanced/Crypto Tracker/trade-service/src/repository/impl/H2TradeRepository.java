package repository.impl;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;

import domain.Trade;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.TradeRepository;

public class H2TradeRepository implements TradeRepository {
    private static final Logger log = LoggerFactory.getLogger("h2-repo");

    private static String INIT_DB =
        "CREATE TABLE trades (" +
            "id varchar(48), " +
            "trade_timestamp long, " +
            "price float, " +
            "amount float, " +
            "currency varchar(8)," +
            "market varchar(64))";

    private static final String TRADES_COUNT_QUERY = "SELECT COUNT(*) as cnt FROM trades";

    private static final String INSERT_TRADE_QUERY =
        "INSERT INTO trades (id, trade_timestamp, price, amount, currency, market) " +
        "VALUES ($1, $2, $3, $4, $5, $6)";

    private final Mono<? extends Connection> h2Client;

    public H2TradeRepository(ConnectionFactory connectionFactory) {
        h2Client = Mono.fromDirect(connectionFactory.create());
        initDB();
        pingDB();
        reportDbStatistics();
    }

    private void initDB() {
        Mono.usingWhen(
                h2Client,
                t -> Mono.fromDirect(t.createStatement(INIT_DB).execute())
                        .flatMap(result -> Mono.fromDirect(result.getRowsUpdated()))
                        .then(Mono.fromRunnable(() -> log.info("DB SCHEMA WAS INITIALIZED"))),
                Connection::close
        ).block();
    }

    private void pingDB() {
        Flux.usingWhen(
                h2Client,
                t -> Flux.from(t.createStatement("SELECT 6").execute()).map(result -> result.map((row, metadata) -> row.get(0))),
                Connection::close
            )
            .doOnNext(e -> log.warn("RESULT FOR SELECT 6 QUERY: " + e))
            .subscribe();
    }

    // Stats: log the amount of stored trades to log every 5 seconds
    private void reportDbStatistics() {
        Flux.interval(Duration.ofSeconds(5))
            .flatMap(i -> this.getTradeStats())
            .doOnNext(count -> log.info("------------- [DB STATS] ------------ Trades stored in DB: " + count))
            .subscribe();
    }

    @Override
    public Mono<Void> saveAll(List<Trade> trades) {
        return this
            .storeTradesInDb(trades)
            .doOnNext(e -> log.info("--- [DB] --- Inserted " + e + " trades into DB"))
            .then();
    }

    private Mono<Long> getTradeStats() {
        return
                Mono.usingWhen(
                        h2Client,
                        t -> Mono.fromDirect(t.createStatement(TRADES_COUNT_QUERY).execute())
                                 .flatMap(result -> Mono.fromDirect(result.map(row -> row.get(0, Long.class)))),
                        Connection::close
                );
    }

    private Mono<Integer> storeTradesInDb(List<Trade> trades) {
        return Mono.usingWhen(
                h2Client,
                t -> Mono.usingWhen(
                        t.beginTransaction(),
                        __ -> Mono.fromDirect(buildInsertStatement(t, trades).execute())
                                .flatMap(result -> Mono.fromDirect(result.getRowsUpdated())),
                        __ -> t.commitTransaction(),
                        (__, ex) -> t.rollbackTransaction(),
                        __ -> t.rollbackTransaction()
                ),
                Connection::close
        );
    }

    // --- Helper methods --------------------------------------------------

    // TODO: Use this method in storeTradesInDb(...) method
    private Statement buildInsertStatement(Connection handle, List<Trade> trades) {
        Statement update = handle.createStatement(INSERT_TRADE_QUERY);

        Iterator<Trade> tradeIterator = trades.iterator();
        for (int i = 0; tradeIterator.hasNext(); i++) {
            Trade trade = tradeIterator.next();
            if (i != 0) {
                update.add();
            }
            update
                .bind("$1", trade.getId())
                .bind("$2", trade.getTimestamp())
                .bind("$3", trade.getPrice())
                .bind("$4", trade.getAmount())
                .bind("$5", trade.getCurrency())
                .bind("$6", trade.getCurrency());
        }
        return update;
    }

}
