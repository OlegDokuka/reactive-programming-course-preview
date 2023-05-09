import java.util.HashMap;

import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

public class ExecutionOptimizationTask {

	/* TODO: use AsyncRestTemplate instead */
	static RestTemplate restTemplate;

	public static PageView handle(String userId) {
		User user = restTemplate.getForObject("{userId}/user", User.class, userId);
		Cart cart = restTemplate.getForObject("{userId}/cart", Cart.class, userId);
		Wallet wallet = restTemplate.getForObject("{userId}/wallet", Wallet.class, userId);

		HashMap<String, Object> data = new HashMap<>();
		data.put("user", user);
		data.put("cart", cart);
		data.put("wallet", wallet);

		return new PageView("admin_page", data);
	}
}