import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserActivityUtils {

	public static Mono<Product> findMostExpansivePurchase(Flux<Order> ordersHistory,
			ProductsCatalog productsCatalog) {

		return Mono.error(new ToDoException());
	}
}
