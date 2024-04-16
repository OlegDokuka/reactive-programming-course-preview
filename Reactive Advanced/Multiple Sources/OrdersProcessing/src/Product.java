import java.math.BigDecimal;
import java.util.Objects;

public class Product {
	final String     id;
	final String     name;
	final BigDecimal price;
	final String ccy;

	public Product(String id, String name, BigDecimal price, String ccy) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.ccy = ccy;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getCcy() {
		return ccy;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Product product = (Product) o;
		return Objects.equals(id, product.id) && Objects.equals(name,
				product.name) && Objects.equals(price, product.price) && Objects.equals(
				ccy,
				product.ccy);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(id);
		result = 31 * result + Objects.hashCode(name);
		result = 31 * result + Objects.hashCode(price);
		result = 31 * result + Objects.hashCode(ccy);
		return result;
	}
}
