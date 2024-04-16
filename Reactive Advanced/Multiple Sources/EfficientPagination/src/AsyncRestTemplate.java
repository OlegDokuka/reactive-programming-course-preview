import java.util.concurrent.CompletableFuture;

public interface AsyncRestTemplate {

    <T> CompletableFuture<T> getForObjectAsync(String uri, Class<T> clazz, Object... data);
}
