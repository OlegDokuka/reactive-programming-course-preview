import reactor.core.publisher.Flux;

public class Page<T> {

	final int     currentPage;
	final boolean hasMorePages;
	final Flux<T> items;

	public Page(int currentPage, boolean hasMorePages, Flux<T> items) {
		this.currentPage = currentPage;
		this.hasMorePages = hasMorePages;
		this.items = items;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public boolean hasMorePages() {
		return hasMorePages;
	}

	public Flux<T> getItems() {
		return items;
	}
}
