public class ErrorResult implements Result {

	private final Throwable error;

	public ErrorResult(Throwable throwable) {
		error = throwable;
	}

	@Override
	public Throwable error() {
		return error;
	}

	@Override
	public long transactionId() {
		return -1;
	}
}
