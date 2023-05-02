import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FindElementsInRepositoryTask {

	 static UsersRepository usersRepository;

	public static Flux<User> findUsersFrom(String country) {
		return usersRepository.findAll();
	}
}