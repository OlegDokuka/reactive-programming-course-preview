import java.util.Objects;

public class CustomerInfo {
	final String userId;
	final String bankAccountNumber;

	public CustomerInfo(String userId, String bankAccountNumber) {
		this.userId = userId;
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getUserId() {
		return userId;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		CustomerInfo info = (CustomerInfo) o;
		return Objects.equals(userId, info.userId) && Objects.equals(bankAccountNumber,
				info.bankAccountNumber);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(userId);
		result = 31 * result + Objects.hashCode(bankAccountNumber);
		return result;
	}
}
