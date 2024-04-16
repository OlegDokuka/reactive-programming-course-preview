import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

public class MovieServiceTests {

	@Test
	public void checkAllPagesAreResolved() {
		TestPublisher<Movie> publisher1 = TestPublisher.create();
		TestPublisher<Movie> publisher2 = TestPublisher.create();
		TestPublisher<Movie> publisher3 = TestPublisher.create();
		MoviesRepository moviesRepository = Mockito.mock(MoviesRepository.class);
		MovieService.moviesRepository = moviesRepository;
		Mockito.when(moviesRepository.getPageOfMovies(Mockito.anyInt()))
		       .then(args -> Mono.just(new Page<>(1, true, publisher1.flux())))
		       .then(args -> Mono.just(new Page<>(2, true, publisher2.flux())))
		       .then(args -> Mono.just(new Page<>(3, false, publisher3.flux())));
		StepVerifier.create(MovieService.getMovies())
		            .expectSubscription()
		            .then(() -> publisher1.assertSubscribers(1))
		            .then(() -> publisher2.assertNoSubscribers())
		            .then(() -> publisher3.assertNoSubscribers())
		            .then(() -> publisher1.next(new Movie("1", "James Bond: SkyFall")))
		            .expectNext(new Movie("1", "James Bond: SkyFall"))
		            .then(() -> publisher1.assertSubscribers(1))
		            .then(() -> publisher2.assertNoSubscribers())
		            .then(() -> publisher3.assertNoSubscribers())
		            .then(() -> publisher1.complete())
		            .then(() -> publisher2.assertSubscribers(1))
		            .then(() -> publisher3.assertNoSubscribers())
		            .then(() -> publisher2.next(new Movie("2", "Lion the King")))
		            .expectNext(new Movie("2", "Lion the King"))
		            .then(() -> publisher2.assertSubscribers(1))
		            .then(() -> publisher3.assertNoSubscribers())
		            .then(() -> publisher2.complete())
		            .then(() -> publisher3.assertSubscribers(1))
		            .then(() -> publisher3.next(new Movie("3", "Matrix 3")))
		            .expectNext(new Movie("3", "Matrix 3"))
		            .then(() -> publisher3.complete())
		            .expectComplete()
		            .verify();

	}
}