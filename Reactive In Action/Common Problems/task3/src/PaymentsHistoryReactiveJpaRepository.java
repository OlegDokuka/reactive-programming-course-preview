import reactor.core.publisher.Flux;

public interface PaymentsHistoryReactiveJpaRepository {

	Flux<Payment> findAllByUserId(String userId);
}
