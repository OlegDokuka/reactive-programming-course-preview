import reactor.core.publisher.Flux;

public interface UsersRepository {

  Flux<User> findAll();
}
