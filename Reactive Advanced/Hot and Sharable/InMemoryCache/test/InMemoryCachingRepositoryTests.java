import java.time.Duration;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import reactor.core.publisher.*;
import reactor.test.StepVerifier;

public class InMemoryCachingRepositoryTests {

    @Test
    public void testSolution2() {
        UsersProfileRepository usersProfileRepository = Mockito.mock(UsersProfileRepository.class);
        Mockito.when(usersProfileRepository.find(Mockito.any()))
                .thenAnswer(a -> Mono.delay(Duration.ofMillis(ThreadLocalRandom.current().nextLong(100, 500))).map(i -> new UsersProfile(a.getArgument(0), UUID.randomUUID().toString())));

		InMemoryCachingRepositoryTask.usersProfileRepository = usersProfileRepository;

		Flux.range(0, 1000)
			.map(i -> "key" + (i % 5))
			.flatMap(InMemoryCachingRepositoryTask::find, 1000)
			.distinct()
			.as(StepVerifier::create)
			.expectNextCount(5)
			.verifyComplete();
    }
}