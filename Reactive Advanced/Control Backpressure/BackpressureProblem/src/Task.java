import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

public class Task {

	public static Publisher<Tuple2<Character, Integer>> groupWordsByFirstLatter(Flux<String> words) {
		return Flux.error(new ToDoException());
	}

	public static Flux<GroupedFlux<Character, String>> groupByFirstLetter(Flux<String> words) {
		return Flux.error(new ToDoException());
	}

	public static Flux<Tuple2<Character, Integer>> countLettersInWordsInGroup(Flux<GroupedFlux<Character,
			String>> groupedWords) {
		return Flux.error(new ToDoException());
	}
}