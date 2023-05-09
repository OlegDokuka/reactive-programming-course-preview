import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

public class CPTask2Tests {

	@Test
	@SuppressWarnings("unchecked")
	public void findVideoTest() {
		List<Server> servers = new ServersCatalogue().list()
		                                             .stream()
		                                             .map(MockableServer::new)
		                                             .collect(Collectors.toList());
		ServersCatalogue serversCatalogue = Mockito.mock(ServersCatalogue.class);
		Mockito.when(serversCatalogue.list())
		       .thenReturn(servers);

		MediaService service = new MediaService(serversCatalogue);
		StepVerifier.create(service.findVideo("test"))
		            .expectSubscription()
		            .expectNextCount(1)
		            .verifyComplete();

		long count = servers.stream()
		                    .map(MockableServer.class::cast)
		                    .map(MockableServer::getProbe)
		                    .filter(PublisherProbe::wasCancelled)
		                    .count();

		Assertions.assertThat(count)
		          .isBetween(servers.size() - 1L, ((long) servers.size()));
	}

	static final class MockableServer extends Server {

		private final Server                delegate;
		private       PublisherProbe<Video> probe;

		public MockableServer(Server delegate) {
			super("");
			this.delegate = delegate;
		}

		public PublisherProbe<Video> getProbe() {
			return probe;
		}

		@Override
		public Mono<Video> searchOne(String name) {
			return (probe = PublisherProbe.of(delegate.searchOne(name))).mono();
		}
	}
}