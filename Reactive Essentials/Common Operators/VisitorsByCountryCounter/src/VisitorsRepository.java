import reactor.core.publisher.Flux;

public interface VisitorsRepository {

  Flux<User> findAll();
}
