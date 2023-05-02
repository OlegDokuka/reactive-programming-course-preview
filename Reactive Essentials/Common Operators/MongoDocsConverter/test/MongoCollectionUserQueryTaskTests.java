import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MongoCollectionUserQueryTaskTests {

  @Test
  public void testResultIsCorrect() {
    MongoCollection<Document> mongoCollection = Mockito.mock(MongoCollection.class);
    FindPublisher<Document> findPublisher = Mockito.mock(FindPublisher.class);

    LocalDateTime registered = LocalDateTime.now().minusDays(100);
    LocalDateTime lastViewed = LocalDateTime.now();
    UUID uuid = UUID.randomUUID();

    MongoCollectionUserQueryTask.collection = mongoCollection;

    Mockito.doAnswer(a -> {
      Flux.just(
          new Document()
              .append("id", uuid.toString())
              .append("login", "test")
              .append("password", "aed%123&3221as")
              .append("registered", registered.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
              .append("last_viewed", lastViewed.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
      ).subscribe(a.<Subscriber<Document>>getArgument(0));
      return null;
    }).when(findPublisher).subscribe(Mockito.any());

    Mockito.when(mongoCollection.find()).thenReturn(findPublisher);

    StepVerifier.create(MongoCollectionUserQueryTask.findUsers())
        .expectNext(new User(uuid, "test", "", lastViewed, registered))
        .expectComplete()
        .verify();
  }
}