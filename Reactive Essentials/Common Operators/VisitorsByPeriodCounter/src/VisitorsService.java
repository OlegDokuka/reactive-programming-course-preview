import reactor.core.publisher.Flux;

public interface VisitorsService {

  Flux<User> newVisitiorsStream();
}
