import java.util.List;
import java.util.Objects;

public class Order {

	final String        id;
	final List<Product> products;
	final CustomerInfo  customerInfo;

	public Order(String id, List<Product> products, CustomerInfo info) {
		this.id = id;
		this.products = products;
		this.customerInfo = info;
	}

	public String getId() {
		return id;
	}

	public List<Product> getProducts() {
		return products;
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

		Order order = (Order) o;
		return Objects.equals(id, order.id) && Objects.equals(products,
				order.products) && Objects.equals(customerInfo, order.customerInfo);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
