import org.junit.jupiter.api.Test;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

public class BackpressureTask4Tests {

	@Test
	public void testSolution() {
		TestPublisher<String> publisher = TestPublisher.create();

		StepVerifier
			.withVirtualTime(() -> Task.keepBackpressureForLongRunningOps(
				publisher.flux(), q -> {
						Sinks.One<StatisticSnapshot> sProducer3 = Sinks
							.unsafe()
							.one();

						return sProducer3
							.asMono()
							.doOnRequest(r -> sProducer3.emitValue(new TestStatistic(q), Sinks.EmitFailureHandler.FAIL_FAST));
					}), 0)
			.expectSubscription()
			.then(() -> {
				publisher.next("test0");
				publisher.next("test1");
				publisher.next("test2");
				publisher.next("test3");
				publisher.next("test4");
			})
			.thenRequest(1)
			.expectNext(new TestStatistic("test0"))
			.thenRequest(1)
			.expectNext(new TestStatistic("test1"))
			.then(() -> publisher.next("test5"))
			.then(() -> publisher.next("test6"))
			.thenRequest(1)
			.expectNext(new TestStatistic("test4"))
			.thenRequest(1)
			.expectNext(new TestStatistic("test5"))
			.thenCancel()
			.verify();
	}

	static class TestStatistic implements StatisticSnapshot {

		final String query;

		TestStatistic(String query) {
			this.query = query;
		}

		@Override
		public int hashCode() {
			return query.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			TestStatistic statistic = (TestStatistic) o;

			return query.equals(statistic.query);
		}

		@Override
		public String toString() {
			return "TestStatistic{" + "query='" + query + '\'' + '}';
		}
	}
}