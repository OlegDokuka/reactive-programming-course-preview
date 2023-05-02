import java.util.concurrent.CompletableFuture;

public interface HttpClient {

  <P, R> R postEntity(String uri, P payload, Class<R> clazz);

  <P, R> CompletableFuture<R> postEntityAsync(String uri, P payload, Class<R> clazz);
}
