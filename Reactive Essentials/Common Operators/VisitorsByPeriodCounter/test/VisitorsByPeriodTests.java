import java.time.Duration;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

public class VisitorsByPeriodTests {

    @Test
    public void testSolution() {

        VisitorsService visitorsService = Mockito.mock(VisitorsService.class);

        VisitorsByPeriodTask.visitorsService = visitorsService;

        StepVerifier.withVirtualTime(() -> {
                    Flux<User> probe = Flux.just(new User("1", "Poland"), new User("2", "Ukraine"), new User("3", "Germany"))
                            .concatWith(Flux.defer(() -> Flux.just(new User("4", "Netherlands"), new User("5", "Poland"), new User("6", "Germany"),
                                    new User("7", "Germany")).delaySubscription(Duration.ofHours(1))))
                            .concatWith(Flux.defer(() -> Flux.<User>empty()).delaySubscription(Duration.ofHours(1)));

                    Mockito.when(visitorsService.newVisitiorsStream()).thenReturn(probe.log());
                    return VisitorsByPeriodTask.countVisitorsByCountry(Duration.ofHours(1));
                })
                .expectSubscription()
                .expectNoEvent(Duration.ofHours(1))
                .expectNext(3L)
                .expectNoEvent(Duration.ofHours(1))
                .expectNext(4L)
                .expectNext(0L)
                .expectComplete()
                .verify(Duration.ofMillis(100));

    }
}