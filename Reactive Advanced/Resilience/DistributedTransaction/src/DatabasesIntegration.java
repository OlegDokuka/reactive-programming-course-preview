import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class DatabasesIntegration {

	private final DatabaseApi oracleDb;
	private final DatabaseApi fileDb;

	public DatabasesIntegration(DatabaseApi oracleDb, DatabaseApi fileDb) {
		this.oracleDb = oracleDb;
		this.fileDb = fileDb;
	}

	public Mono<Void> storeToDatabases(Flux<Integer> integerFlux) {
		// TODO: Main) Write data to both databases
		// TODO: 1) Ensure Transaction is rolled back in case of failure
		// TODO: 2) Ensure All transactions are rolled back ion case any of write operations fails
		// TODO: 3) Ensure Transaction lasts less than 1 sec

		return Mono.error(new ToDoException());
	}

    static Mono<Result> dbWriteInTransaction(DatabaseApi db, Flux<Integer> dataSource) {
        return Mono.error(new ToDoException());
    }
}
