import java.nio.file.Path;
import java.util.stream.Stream;

public interface Files {
    Stream<String> lines(Path filename);
}
