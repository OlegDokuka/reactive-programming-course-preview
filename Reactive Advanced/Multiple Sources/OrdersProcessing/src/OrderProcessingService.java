import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

public class OrderProcessingService {

	final CurrencyRepository currencyRepository;
	final OrdersRepository   ordersRepository;

	public OrderProcessingService(CurrencyRepository repository,
			OrdersRepository ordersRepository) {
		this.currencyRepository = repository;
		this.ordersRepository = ordersRepository;
	}

	public Flux<Payment> prepareOrdersForPayment() {
		return Flux.error(new ToDoException());
	}

	static Payment composeFrom(Order order, Map<String, BigDecimal> currencyRates) {
		// Some currency conversion logic is skipped for simplicity purpose
		return new Payment(UUID.randomUUID()
		                       .toString(), order.getId(), currencyRates.get("test"),
				// to check that prices is selected correctly
				order.getCustomerInfo());
	}
}