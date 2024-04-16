import reactor.core.publisher.Flux;

public class MovieService {

	static MoviesRepository moviesRepository;

	public static Flux<Movie> getMovies() {
		return getMoviesSupport(1);
	}

	static Flux<Movie> getMoviesSupport(int pageId) {
		return moviesRepository.getPageOfMovies(pageId).flatMapMany(page -> page.getItems());
	}
}