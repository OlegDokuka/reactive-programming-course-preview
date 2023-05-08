import java.util.List;

import reactor.core.publisher.Mono;

public interface DiscoveryService {
	Mono<List<Server>> activeServers();
}
