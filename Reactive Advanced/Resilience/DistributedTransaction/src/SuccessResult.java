public class SuccessResult implements Result {

	private final long transactionId;

	public SuccessResult(long transactionId) {

		this.transactionId = transactionId;
	}

	@Override
	public Throwable error() {
		return null;
	}

	@Override
	public long transactionId() {
		return transactionId;
	}
}
