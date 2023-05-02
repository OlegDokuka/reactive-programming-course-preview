import com.mongodb.reactivestreams.client.MongoCollection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.UUID;
import org.bson.Document;
import reactor.core.publisher.Flux;

public class MongoCollectionUserQueryTask {

  static MongoCollection<Document> collection;

  public static Flux<User> findUsers() {
    return Flux.error(new ToDoException());
  }

  public static User convert(Document d) {
    return new User(
        UUID.fromString(d.get("id", String.class)),
        d.get("login", String.class),
        "",
        LocalDateTime.parse(d.get("last_viewed", String.class),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        LocalDateTime.parse(d.get("registered", String.class),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    );
  }
}