import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsersProfileRepository {

  Mono<UsersProfile> find(String userID);
}
