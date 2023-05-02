import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DiscoveryService {

  List<String> discoverAddressesFor(String serviceName);

  CompletableFuture<List<String>> asyncDiscoverAddressesFor(String serviceName);
}
