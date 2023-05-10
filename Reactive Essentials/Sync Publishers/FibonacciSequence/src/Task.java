import reactor.core.publisher.Flux;

public class Task {

	public static Flux<Long> createSequence() {
		return Flux.error(new ToDoException());
	}

	static class State {

		final State previous;
		final long  value;
		final long  iteration;

		State(long iteration, long value, State previous) {
			this.iteration = iteration;
			this.previous = previous;
			this.value = value;
		}
	}

	static final State STATE_INITIAL  = new State(1, 1, new State(0, 0, null));
}