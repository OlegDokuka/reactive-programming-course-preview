import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class CPTask1Tests {

	@Test
	public void retrievingTotalPriceTest() {
		ProductsCatalog productsCatalog = Mockito.mock(ProductsCatalog.class);
		Mockito.when(productsCatalog.findById(Mockito.anyString()))
		       .then(a -> new Product(a.getArgument(0), a.getArgument(0), 100));
		List<String> productsIds = Arrays.asList("123", "321", "1235", "1312");

		Mono<Long> totalPrice =
				new Order("test", "test", productsIds, productsCatalog).getTotalPrice();

		StepVerifier.create(totalPrice)
		            .expectSubscription()
		            .expectNext(400L)
		            .expectComplete()
		            .verify(Duration.ofMillis(100));
	}

	@Test
	public void findUsersMostExpensivePurchaseTest() {
		ProductsCatalog productsCatalog = Mockito.mock(ProductsCatalog.class);
		Mockito.when(productsCatalog.findById(Mockito.anyString()))
		       .then(a -> new Product(a.getArgument(0),
				       a.getArgument(0),
				       Long.parseLong(a.getArgument(0))));

		Mono<Product> mostExpansivePurchase =
				UserActivityUtils.findMostExpansivePurchase(Flux.just(new Order("1",
								"test",
								Arrays.asList("1", "2", "3"),
								productsCatalog),
						new Order("2", "test", Arrays.asList("4", "5"), productsCatalog),
						new Order("3", "test", Arrays.asList("6"), productsCatalog)),
						productsCatalog);
		StepVerifier.create(mostExpansivePurchase)
		            .expectNext(new Product("6", "6", 6))
		            .expectComplete()
		            .verify(Duration.ofMillis(1000));
	}
}