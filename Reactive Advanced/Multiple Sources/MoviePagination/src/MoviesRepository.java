import reactor.core.publisher.Mono;

public interface MoviesRepository {
	Mono<Page<Movie>> getPageOfMovies(int pageId);
}
