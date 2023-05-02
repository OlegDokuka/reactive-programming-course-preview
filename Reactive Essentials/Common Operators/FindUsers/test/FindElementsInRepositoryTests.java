import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

public class FindElementsInRepositoryTests {

  @Test
  public void testSolution() {
    PublisherProbe<User> probe = PublisherProbe.of(
        Flux.just(new User("1", "Poland"), new User("2", "Ukraine"), new User("3", "Germany"),
            new User("4", "Netherlands")));

    UsersRepository usersRepository = Mockito.mock(UsersRepository.class);
    Mockito.when(usersRepository.findAll()).thenReturn(probe.flux());

    FindElementsInRepositoryTask.usersRepository = usersRepository;

    FindElementsInRepositoryTask.findUsersFrom("Ukraine")
        .as(StepVerifier::create)
        .expectNext(new User("2", "Ukraine"))
        .expectComplete()
        .verify(Duration.ofMillis(100));

    FindElementsInRepositoryTask.findUsersFrom("Poland")
        .as(StepVerifier::create)
        .expectNext(new User("1", "Poland"))
        .expectComplete()
        .verify(Duration.ofMillis(100));

    FindElementsInRepositoryTask.findUsersFrom("Netherlands")
        .as(StepVerifier::create)
        .expectNext(new User("4", "Netherlands"))
        .expectComplete()
        .verify(Duration.ofMillis(100));

    FindElementsInRepositoryTask.findUsersFrom("USA")
        .as(StepVerifier::create)
        .expectError(UserNotFoundException.class)
        .verify(Duration.ofMillis(100));

    probe.assertWasSubscribed();
    probe.assertWasRequested();
  }
}