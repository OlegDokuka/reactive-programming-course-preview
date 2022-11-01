import java.util.concurrent.CompletableFuture;

public interface AsyncRestTemplate {

    <T> CompletableFuture<T> getForObject(String uri, Class<T> clazz);
}
