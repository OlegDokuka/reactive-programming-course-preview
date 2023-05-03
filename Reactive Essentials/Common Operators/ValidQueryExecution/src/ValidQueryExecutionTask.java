import reactor.core.publisher.Mono;

import java.time.format.DateTimeFormatter;

public class ValidQueryExecutionTask {

    static Validator validator;
    static R2dbcTemplate r2dbcTemplate;

    public static Mono<Void> add(Mono<User> userMono) {
        return r2dbcTemplate.insertQuery(insertQueryFrom(null))
                .then();
    }

    static String insertQueryFrom(User user) {
        return String.format("INSERT INTO USERS (email, nickname, password, birthday) VALUES('%s', '%s', '%s', %s)",
                user.email,
                user.nickname,
                user.password,
                user.birthday.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}