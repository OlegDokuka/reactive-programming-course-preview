import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

public class OrderProcessingServiceTests {

	@Test
	public void testSolution() {
		TestPublisher<Order> orderTestPublisher = TestPublisher.create();
		TestPublisher<Map<String, BigDecimal>> firstCurrenciesTestPublisher =
				TestPublisher.create();
		TestPublisher<Map<String, BigDecimal>> currenciesTestPublisher =
				TestPublisher.create();
		OrdersRepository ordersRepository = Mockito.mock(OrdersRepository.class);
		CurrencyRepository currencyRepository = Mockito.mock(CurrencyRepository.class);

		Mockito.when(ordersRepository.ordersStream())
		       .thenReturn(orderTestPublisher.flux().log());
		Mockito.when(currencyRepository.currencyRates())
		       .thenReturn(firstCurrenciesTestPublisher.mono());
		Mockito.when(currencyRepository.currenciesRatesChanges())
		       .thenReturn(currenciesTestPublisher.flux());
		OrderProcessingService service =
				new OrderProcessingService(currencyRepository, ordersRepository);
		StepVerifier.create(service.prepareOrdersForPayment())
		            .expectSubscription()
		            .then(() -> {
			            orderTestPublisher.assertSubscribers(1);
			            firstCurrenciesTestPublisher.assertSubscribers(1);
			            currenciesTestPublisher.assertSubscribers(1);
		            })
		            .then(() -> {
			            orderTestPublisher.next(new Order("1",
					            Collections.emptyList(),
					            new CustomerInfo("test", "test-test")));
			            orderTestPublisher.assertSubscribers(1);
			            firstCurrenciesTestPublisher.assertSubscribers(1);
			            currenciesTestPublisher.assertSubscribers(1);
		            })
		            .expectNoEvent(Duration.ofMillis(100))
		            .then(() -> {
			            firstCurrenciesTestPublisher.next(Collections.singletonMap("test",
					            BigDecimal.ONE));
			            firstCurrenciesTestPublisher.complete();

			            firstCurrenciesTestPublisher.assertNoSubscribers();
			            orderTestPublisher.assertSubscribers(1);
			            currenciesTestPublisher.assertSubscribers(1);
		            })
		            .assertNext(p -> {
			            Assertions.assertThat(p.getOrderId())
			                      .isEqualTo("1");
			            Assertions.assertThat(p.getAmount())
			                      .isCloseTo(BigDecimal.ONE,
					                      Percentage.withPercentage(1));
		            })
		            .then(() -> {
			            orderTestPublisher.next(new Order("2",
					            Collections.emptyList(),
					            new CustomerInfo("test", "test-test")));
			            orderTestPublisher.next(new Order("3",
					            Collections.emptyList(),
					            new CustomerInfo("test", "test-test")));
			            orderTestPublisher.next(new Order("4",
					            Collections.emptyList(),
					            new CustomerInfo("test", "test-test")));
		            })
		            .assertNext(p -> {
			            Assertions.assertThat(p.getOrderId())
			                      .isEqualTo("2");
			            Assertions.assertThat(p.getAmount())
			                      .isCloseTo(BigDecimal.ONE,
					                      Percentage.withPercentage(1));
		            })
		            .assertNext(p -> {
			            Assertions.assertThat(p.getOrderId())
			                      .isEqualTo("3");
			            Assertions.assertThat(p.getAmount())
			                      .isCloseTo(BigDecimal.ONE,
					                      Percentage.withPercentage(1));
		            })
		            .assertNext(p -> {
			            Assertions.assertThat(p.getOrderId())
			                      .isEqualTo("4");
			            Assertions.assertThat(p.getAmount())
			                      .isCloseTo(BigDecimal.ONE,
					                      Percentage.withPercentage(1));
		            })
		            .then(() -> {
			            currenciesTestPublisher.next(Collections.singletonMap("test",
					            BigDecimal.TEN));
		            })
		            .expectNoEvent(Duration.ofMillis(100))
		            .then(() -> {
			            currenciesTestPublisher.next(Collections.singletonMap("test",
					            BigDecimal.valueOf(20)));
		            })
		            .expectNoEvent(Duration.ofMillis(100))
		            .then(() -> {
			            orderTestPublisher.next(new Order("5",
					            Collections.emptyList(),
					            new CustomerInfo("test", "test-test")));
		            })
		            .assertNext(p -> {
			            Assertions.assertThat(p.getOrderId())
			                      .isEqualTo("5");
			            Assertions.assertThat(p.getAmount())
			                      .isCloseTo(BigDecimal.valueOf(20),
					                      Percentage.withPercentage(1));
		            })
		            .thenCancel()
		            .verify();
	}
}