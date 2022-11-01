import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class CommonPublishersTask3Tests {

	@Test
	public void testSolution() {
		PropertiesSourceTask.settings = Mockito.mock(Properties.class);
		Mockito.when(PropertiesSourceTask.settings.asList()).thenReturn(Stream.of("1", "2", "3").map(n -> new Property<String>() {
			@Override
			public String name() {
				return n;
			}

			@Override
			public String value() {
				return n + "-v";
			}
		}).collect(Collectors.toList()));
		Object sequence = PropertiesSourceTask.createSequence();

		if (!(sequence instanceof Flux)) {
			Assertions.fail("Unexpected sequence type");
		}

		StepVerifier.create(((Flux<Property<?>>) sequence).map(Property::name))
		            .expectNext("1", "2", "3")
		            .expectComplete()
		            .verify(Duration.ofMillis(100));
	}
}