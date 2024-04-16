import reactor.core.publisher.Flux;

public interface OrdersRepository {

	Flux<Order> ordersStream();
}