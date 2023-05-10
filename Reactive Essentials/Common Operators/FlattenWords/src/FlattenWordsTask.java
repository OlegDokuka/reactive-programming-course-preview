import java.util.ArrayList;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FlattenWordsTask {

	public static List<Character> flattenWordsIntoLetters(List<String> words) {
		List<Character> result = new ArrayList<>();
        for (String word : words) {
            char[] letters = word.toCharArray();
            for (char letter : letters) {
                result.add(letter);
            }
        }
        return result;
	}
}