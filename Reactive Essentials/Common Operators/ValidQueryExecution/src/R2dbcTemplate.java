import reactor.core.publisher.Mono;

public interface R2dbcTemplate {

    Mono<Integer> insertQuery(String query);
}
