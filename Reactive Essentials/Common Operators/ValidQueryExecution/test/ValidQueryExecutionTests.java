import java.time.Duration;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

public class ValidQueryExecutionTests {

	@Test
	public void testSolution() {
		Validator validator = Mockito.mock(Validator.class);
		R2dbcTemplate r2dbcTemplate = Mockito.mock(R2dbcTemplate.class);

		ValidQueryExecutionTask.validator = validator;
		ValidQueryExecutionTask.r2dbcTemplate = r2dbcTemplate;

		Mockito.when(validator.validate()).thenAnswer(a -> Mono.delay(Duration.ofSeconds(1)).then());
		Mockito.when(r2dbcTemplate.insertQuery(Mockito.any())).thenAnswer(a -> Mono.delay(Duration.ofSeconds(1)).map(Long::intValue));

		PublisherProbe<User>[] probe = new PublisherProbe[1];

		StepVerifier.withVirtualTime(() -> {
			            probe[0] = PublisherProbe.of(Mono.delay(Duration.ofSeconds(1))
			                                  .thenReturn(new User("test@test.test",
					                                  "testnick",
					                                  "testpass",
					                                  LocalDate.of(1900, 10, 21))));
			            return ValidQueryExecutionTask.add(probe[0].mono());
		            })
				.expectSubscription()
				.then(() -> probe[0].assertWasNotSubscribed())
				.expectNoEvent(Duration.ofSeconds(1))
				.then(() -> {
					probe[0].assertWasSubscribed();
					Mockito.verifyNoInteractions(r2dbcTemplate);
				})
				.expectNoEvent(Duration.ofSeconds(1))
				.then(() -> {
					ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
					Mockito.verify(r2dbcTemplate).insertQuery(captor.capture());
					Assertions.assertThat(captor.getValue())
					          .isEqualTo("INSERT INTO USERS (email, nickname, password, birthday) VALUES('test@test.test', 'testnick', 'testpass', 1900-10-21)");
				})
				.expectNoEvent(Duration.ofSeconds(1))
				.expectComplete()
				.verify();
	}


	@Test
	public void testSolution2() {
		Validator validator = Mockito.mock(Validator.class);
		R2dbcTemplate r2dbcTemplate = Mockito.mock(R2dbcTemplate.class);

		ValidQueryExecutionTask.validator = validator;
		ValidQueryExecutionTask.r2dbcTemplate = r2dbcTemplate;

		Mockito.when(validator.validate()).thenAnswer(a -> Mono.delay(Duration.ofSeconds(1)).then(Mono.error(new IllegalArgumentException("boom"))));
		Mockito.when(r2dbcTemplate.insertQuery(Mockito.any())).thenAnswer(a -> Mono.delay(Duration.ofSeconds(1)).map(Long::intValue));

		PublisherProbe<User>[] probe = new PublisherProbe[1];

		StepVerifier.withVirtualTime(() -> {
			            probe[0] = PublisherProbe.of(Mono.delay(Duration.ofSeconds(1))
			                                             .thenReturn(new User("test@test.test",
					                                             "testnick",
					                                             "testpass",
					                                             LocalDate.of(1900, 10, 21))));
			            return ValidQueryExecutionTask.add(probe[0].mono());
		            })
		            .expectSubscription()
		            .then(() -> probe[0].assertWasNotSubscribed())
		            .expectNoEvent(Duration.ofSeconds(1))
		            .then(() -> {
			            probe[0].assertWasNotSubscribed();
			            Mockito.verifyNoInteractions(r2dbcTemplate);
		            })
		            .expectError()
		            .verify();
	}
}