import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class TimezonesDbClientTests {

	@Test
	public void ensuresPaginationIsResolved() {
		AsyncRestTemplate asyncRestTemplate = Mockito.mock(AsyncRestTemplate.class);
		TimezonesDbClient client = new TimezonesDbClient("test", asyncRestTemplate);

		AtomicInteger cnt = new AtomicInteger();
		TimezonedbResponseZone timezone = new TimezonedbResponseZone("US",
				"United States",
				"Alabama",
				"Athens",
				"America/Chicago",
				"CST",
				-21600,
				"0",
				1699167600,
				1706881503,
				"2024-02-02 13:45:03",
				1710057600,
				"CDT");
		Mockito.when(asyncRestTemplate.getForObjectAsync(Mockito.anyString(),
				       Mockito.any(),
				       Mockito.any()))
		       .then(args -> CompletableFuture.completedFuture(new TimezonedbResponse("OK",
				       "",
				       3,
				       cnt.incrementAndGet(),
				       Collections.singletonList(timezone))));

		StepVerifier.create(client.findPageOfTimezonesFor("test"))
		            .expectSubscription()
		            .expectNext(Arrays.asList(timezone, timezone, timezone))
		            .verifyComplete();

		Assertions.assertThat(cnt)
		          .hasValue(3);
	}

	@Test
	@Timeout(3)
	public void ensuresPaginationIsResolvedConcurrently() {
		AsyncRestTemplate asyncRestTemplate = Mockito.mock(AsyncRestTemplate.class);
		TimezonesDbClient client = new TimezonesDbClient("test", asyncRestTemplate);

		AtomicInteger cnt = new AtomicInteger();
		TimezonedbResponseZone timezone = new TimezonedbResponseZone("US",
				"United States",
				"Alabama",
				"Athens",
				"America/Chicago",
				"CST",
				-21600,
				"0",
				1699167600,
				1706881503,
				"2024-02-02 13:45:03",
				1710057600,
				"CDT");
		Mockito.when(asyncRestTemplate.getForObjectAsync(Mockito.anyString(),
				       Mockito.any(),
				       Mockito.any()))
		       .then(args -> {
			       int page = cnt.incrementAndGet();
			       return Mono.delay(Duration.ofSeconds(page))
			                  .thenReturn(new TimezonedbResponse("OK",
					                  "",
					                  4,
					                  page,
					                  Collections.singletonList(timezone)))
			                  .toFuture();
		       });

		StepVerifier.withVirtualTime(() -> client.findPageOfTimezonesFor("test"))
		            .expectSubscription()
					.thenAwait(Duration.ofSeconds(1 + 4)) // 1 sec to resolve first and
					// then no more than 4 secs to resolve the others
		            .expectNext(Arrays.asList(timezone, timezone, timezone, timezone))
		            .verifyComplete();

		Assertions.assertThat(cnt)
		          .hasValue(4);
	}

	@Test
	@Timeout(1)
	public void ensuresPaginationStops() {
		AsyncRestTemplate asyncRestTemplate = Mockito.mock(AsyncRestTemplate.class);
		TimezonesDbClient client = new TimezonesDbClient("test", asyncRestTemplate);

		AtomicInteger cnt = new AtomicInteger();
		TimezonedbResponseZone timezone = new TimezonedbResponseZone("US",
				"United States",
				"Alabama",
				"Athens",
				"America/Chicago",
				"CST",
				-21600,
				"0",
				1699167600,
				1706881503,
				"2024-02-02 13:45:03",
				1710057600,
				"CDT");
		Mockito.when(asyncRestTemplate.getForObjectAsync(Mockito.anyString(),
				       Mockito.any(),
				       Mockito.any()))
		       .then(args -> {
			       int page = cnt.incrementAndGet();
			       return Mono.delay(Duration.ofSeconds(page))
			                  .thenReturn(new TimezonedbResponse("OK",
					                  "",
					                  1,
					                  page,
					                  Collections.singletonList(timezone)))
			                  .toFuture();
		       });

		StepVerifier.withVirtualTime(() -> client.findPageOfTimezonesFor("test"))
		            .expectSubscription()
		            .thenAwait(Duration.ofSeconds(1)) // 1 sec to resolve first and
		            // then no more than 4 secs to resolve the others
		            .expectNext(Arrays.asList(timezone))
		            .verifyComplete();

		Assertions.assertThat(cnt)
		          .hasValue(1);
		Mockito.verify(asyncRestTemplate, Mockito.atMostOnce()).getForObjectAsync(Mockito.anyString(), Mockito.any(), Mockito.any());
	}

	@Test
	@Timeout(1)
	public void ensuresPaginationCanHandleEmptyResponse() {
		AsyncRestTemplate asyncRestTemplate = Mockito.mock(AsyncRestTemplate.class);
		TimezonesDbClient client = new TimezonesDbClient("test", asyncRestTemplate);

		AtomicInteger cnt = new AtomicInteger();
		Mockito.when(asyncRestTemplate.getForObjectAsync(Mockito.anyString(),
				       Mockito.any(),
				       Mockito.any()))
		       .then(args -> {
			       int page = cnt.incrementAndGet();
			       return Mono.delay(Duration.ofSeconds(page))
			                  .thenReturn(new TimezonedbResponse("FAILED",
					                  "Record not found.",
					                  1,
					                  page,
					                  Collections.emptyList()))
			                  .toFuture();
		       });

		StepVerifier.withVirtualTime(() -> client.findPageOfTimezonesFor("test"))
		            .expectSubscription()
		            .thenAwait(Duration.ofSeconds(1))
		            .verifyComplete();

		Assertions.assertThat(cnt)
		          .hasValue(1);
		Mockito.verify(asyncRestTemplate, Mockito.atMostOnce()).getForObjectAsync(Mockito.anyString(), Mockito.any(), Mockito.any());
	}
}