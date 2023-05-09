import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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
}