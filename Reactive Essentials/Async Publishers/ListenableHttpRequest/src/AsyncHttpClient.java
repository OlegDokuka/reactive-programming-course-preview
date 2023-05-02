
public interface AsyncHttpClient {

    <T> ListenableFuture<T> getForObject(String uri, Class<T> clazz);
}
