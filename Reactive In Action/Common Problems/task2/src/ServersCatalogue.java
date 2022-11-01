import java.util.Arrays;
import java.util.List;

public class ServersCatalogue {

	public List<Server> list() {
		return servers;
	}
	private final static List<Server> servers =
			Arrays.asList(new Server("http://a.servers.com"),
					new Server("http://b.servers.com"),
					new Server("http://c.servers.com"),
					new Server("http://d.servers.com"),
					new Server("http://e.servers.com"),
					new Server("http://f.servers.com"));

}
