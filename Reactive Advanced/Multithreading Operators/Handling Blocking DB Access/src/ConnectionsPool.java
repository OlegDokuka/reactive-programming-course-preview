import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsPool {

	public static ConnectionsPool instance() {
		return connectionsPool;
	}

	private final int           size;
	private final AtomicInteger connectionsCounter = new AtomicInteger();

	public ConnectionsPool(int size) {
		this.size = size;
	}

	public void release() {
		connectionsCounter.decrementAndGet();
	}

	public int size() {
		return size;
	}

	public void tryAcquire() {
		connectionsCounter.accumulateAndGet(1, (current, plus) -> {
			if (current >= size) {
				throw new IllegalStateException("No available connections in the pool");
			}
			return current + plus;
		});
	}

	private static final ConnectionsPool connectionsPool = new ConnectionsPool(20);
}
