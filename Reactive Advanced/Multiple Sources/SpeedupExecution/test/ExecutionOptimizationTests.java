import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.Executors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class ExecutionOptimizationTests {

	@Test
	@Timeout(5)
	public void testSolution() {
		AsyncRestTemplate restTemplate = Mockito.mock(AsyncRestTemplate.class);
		User user = Mockito.mock(User.class);
		Cart cart = Mockito.mock(Cart.class);
		Wallet wallet = Mockito.mock(Wallet.class);

		HashMap<String, Object> data = new HashMap<>();
		data.put("user", user);
		data.put("cart", cart);
		data.put("wallet", wallet);

		ExecutionOptimizationTask.restTemplate = restTemplate;

		Mockito.when(restTemplate.getForObjectAsync(Mockito.any(),
				       Mockito.any(),
				       Mockito.any()))
		       .thenAnswer((a) -> {
			       Object argument = a.getArgument(1);
			       if (argument.equals(User.class)) {
				       return Mono.just(user)
						          .delayElement(Duration.ofSeconds(1))
						          .toFuture();
			       }
			       else if (argument.equals(Cart.class)) {
				       return Mono.just(cart)
						       .delayElement(Duration.ofSeconds(2))
						       .toFuture();
			       }
			       else {
				       return Mono.just(wallet)
						       .delayElement(Duration.ofSeconds(3))
						       .toFuture();
			       }
		       });

		Mockito.when(restTemplate.getForObject(Mockito.any(),
				       Mockito.any(),
				       Mockito.any()))
		       .thenAnswer((a) -> restTemplate.getForObjectAsync(a.getArgument(0),
				                                      a.getArgument(1),
				                                      a.getArgument(2))
		                                      .get());

		StepVerifier.withVirtualTime(() -> Mono.defer(() -> {
		                                           Object response = ExecutionOptimizationTask.handle("helloWorld");

		                                           if (!(response instanceof Mono)) {
		                                               return Mono.just(response);
		                                           }
		                                           else {
		                                               return ((Mono<?>) response);
		                                           }
		                                       }))
		            .expectSubscription()
		            .expectNoEvent(Duration.ofSeconds(3))
		            .expectNext(new PageView("admin_page", data))
		            .expectComplete()
		            .verify();
	}
}