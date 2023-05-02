import java.time.Duration;

import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

public class VisitorsByCountryTests {

  @Test
  public void testSolution() {
    PublisherProbe<User> probe = PublisherProbe.of(
        Flux.just(new User("1", "Poland"), new User("2", "Ukraine"), new User("3", "Germany"),
            new User("4", "Netherlands"), new User("5", "Poland"), new User("6", "Germany"),
            new User("7", "Germany")));

    VisitorsRepository visitorsRepository = Mockito.mock(VisitorsRepository.class);
    Mockito.when(visitorsRepository.findAll()).thenReturn(probe.flux());

    VisitorsByCountryTask.visitorsRepository = visitorsRepository;

    VisitorsByCountryTask.countVisitorsByCountry()
        .as(StepVerifier::create)
        .expectNext(new HashMap<String, Long>() {
          {
            put("Poland", 2L);
            put("Ukraine", 1L);
            put("Germany", 3L);
            put("Netherlands", 1L);
          }
        })
        .expectComplete()
        .verify(Duration.ofMillis(100));

    probe.assertWasSubscribed();
    probe.assertWasRequested();
  }

  @Test
  public void testSolution2() {
    PublisherProbe<User> probe = PublisherProbe.of(
        Flux.empty());

    VisitorsRepository visitorsRepository = Mockito.mock(VisitorsRepository.class);
    Mockito.when(visitorsRepository.findAll()).thenReturn(probe.flux());

    VisitorsByCountryTask.visitorsRepository = visitorsRepository;

    VisitorsByCountryTask.countVisitorsByCountry()
        .as(StepVerifier::create)
        .expectNext(new HashMap<String, Long>() {})
        .expectComplete()
        .verify(Duration.ofMillis(100));

    probe.assertWasSubscribed();
    probe.assertWasRequested();
  }
}