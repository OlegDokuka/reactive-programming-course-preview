import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

public class ContextTask3Tests {

	@Test
	public void testSolution() {
		Mono<String> a = Mono.subscriberContext()
		                     .filter(context -> context.hasKey("a") && !context.hasKey("b"))
		                     .map(context -> context.get("a"));
		Mono<String> b = Mono.subscriberContext()
		                     .filter(context -> context.hasKey("b") && !context.hasKey("a"))
		                     .map(context -> context.get("b"));
		Flux<String> flux = Task.provideCorrectContext(a,
				Context.of("a", "a"),
				b,
				Context.of("b", "b"));

		StepVerifier.create(flux)
		            .expectSubscription()
		            .expectNext("a")
		            .expectNext("b")
		            .verifyComplete();
	}
}