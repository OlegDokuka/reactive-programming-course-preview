import java.util.concurrent.CompletableFuture;

public interface RestTemplate {

    <T> T getForObject(String uri, Class<T> clazz, Object... data);
}
