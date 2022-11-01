import java.util.Objects;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Order {

	private final String           id;
	private final String           userId;
	private final Iterable<String> productsIds;
	private final ProductsCatalog productsCatalog;

	public Order(String id, String userId, Iterable<String> productsIds, ProductsCatalog productsCatalog) {
		this.id = Objects.requireNonNull(id);
		this.userId = Objects.requireNonNull(userId);
		this.productsIds = Objects.requireNonNull(productsIds);
		this.productsCatalog = Objects.requireNonNull(productsCatalog);
	}

	public Mono<Long> getTotalPrice() {
		return Mono.error(new ToDoException());
	}

	public String getId() {
		return this.id;
	}

	public String getUserId() {
		return this.userId;
	}

	public Iterable<String> getProductsIds() {
		return this.productsIds;
	}
}
