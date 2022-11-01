import reactor.core.publisher.Flux;

public class PropertiesSourceTask {

	static Properties settings;

	public static Iterable<Property<?>> createSequence() {
		return settings.asList();
	}
}