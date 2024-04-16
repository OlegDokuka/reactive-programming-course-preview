import java.math.BigDecimal;
import java.util.Objects;

public class Payment {
	final String id;
	final String orderId;
	final BigDecimal amount;
	final CustomerInfo customerInfo;

	public Payment(String id, String orderId, BigDecimal amount, CustomerInfo info) {
		this.id = id;
		this.orderId = orderId;
		this.amount = amount;
		customerInfo = info;
	}

	public String getId() {
		return id;
	}

	public String getOrderId() {
		return orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Payment payment = (Payment) o;
		return Objects.equals(id, payment.id) && Objects.equals(orderId,
				payment.orderId) && Objects.equals(customerInfo, payment.customerInfo);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(id);
		result = 31 * result + Objects.hashCode(orderId);
		result = 31 * result + Objects.hashCode(customerInfo);
		return result;
	}
}
