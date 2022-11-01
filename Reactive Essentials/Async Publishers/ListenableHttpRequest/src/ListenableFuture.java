import java.util.function.BiConsumer;

public interface ListenableFuture<T> {
    void handle(BiConsumer<T, Throwable> handler);
    void cancel();
}
