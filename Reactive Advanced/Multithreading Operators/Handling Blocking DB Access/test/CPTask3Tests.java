import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CPTask3Tests {

	@Test
	public void findVideoTest() {
		// TODO add more tests
		PaymentService service = new PaymentService();

		StepVerifier.create(service.findPayments(Flux.range(1, 100)
		                                             .map(String::valueOf))
		                           .then())
		            .expectSubscription()
		            .verifyComplete();
	}

	@Test
	public void offloadingShouldUseDbPoolThreads() {
		PaymentService service = new PaymentService();

		StepVerifier.create(
				service.findPayments(Flux.just("42"))
				       .map(p -> Thread.currentThread().getName())
				       .take(1))
		            .expectNextMatches(name -> name.contains("db_pool"))
		            .verifyComplete();
	}

	@Test
	public void shouldNotOverflowConnectionsPoolWithManyConcurrentQueries() {
		PaymentService service = new PaymentService();

		StepVerifier.create(
				service.findPayments(Flux.range(1, 1_000).map(String::valueOf)).then())
		            .verifyComplete();
	}

	@Test
	public void threadsUsedShouldNotExceedConnectionsPoolSize() {
		PaymentService service = new PaymentService();
		Set<String> threads = ConcurrentHashMap.newKeySet();

		StepVerifier.create(
				service.findPayments(Flux.range(1, 200).map(String::valueOf))
				       .doOnNext(p -> threads.add(Thread.currentThread().getName()))
				       .then())
		            .verifyComplete();

		// ensure that the number of distinct threads used does not exceed pool size
		int poolSize = ConnectionsPool.instance().size();
		assert threads.size() <= poolSize : "Used threads: " + threads.size() + ", pool size: " + poolSize + ", threads: " + threads;
	}
}