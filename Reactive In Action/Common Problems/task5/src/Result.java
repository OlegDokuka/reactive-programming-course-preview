public interface Result {

	static Result error(Throwable t) {
		return new ErrorResult(t);
	}

	static Result ok(long transactionId) {
		return new SuccessResult(transactionId);
	}

	Throwable error();

	long transactionId();
}
