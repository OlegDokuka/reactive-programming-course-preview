import java.util.List;

public interface Properties {
    <T> Property<T> get(String key);

    List<Property<?>> asList();
}

