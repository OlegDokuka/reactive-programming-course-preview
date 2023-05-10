import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryCachingRepositoryTask {

	static CacheManager<String, UsersProfile> cacheManager = new CacheManager<>();

	static UsersProfileRepository usersProfileRepository;

	public static Mono<UsersProfile> find(String userID) {
		return cacheManager.readOrResolve(userID, usersProfileRepository.find(userID));
	}

	static class CacheManager<K, V> {
		final ConcurrentMap<K, V> data = new ConcurrentHashMap<>();
		final TransactionManager<K, V> transactionManager = new TransactionManager<>();

		Mono<V> readOrResolve(K key, Mono<V> resolver) {
			return /* TODO  implement check of the cache and fallback to resolver if cache is empty. If the cache was empty, add found result into cache. Wrap execution in lock to ensure no calls are overlapping. */ resolver;
		}
	}

	static class TransactionManager<K, V> {
		final ConcurrentMap<K, Mono<V>> actions = new ConcurrentHashMap<>();

		/**
		 * Ensures a given mono is executed without any overlap with others for the same key
		 *
		 * @param key identifier
		 * @param call async operation to run
		 *
		 * @return wrapped
		 */
		Mono<V> lock(K key, Mono<V> call) {
			return /* TODO ensure all calls for the same key are called one after the other. Use map as a storage for active calls. If there is an ongoing call, delay subscription to the given one until previous is done. To subscribe multiple time to the same execution try to cache it. */ call;
		}
	}
}