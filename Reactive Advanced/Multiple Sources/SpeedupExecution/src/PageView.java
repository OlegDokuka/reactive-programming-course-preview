import java.util.Map;

public class PageView {
	final Map<String, Object> data;
	final String viewName;

	public PageView(String viewName, Map<String, Object> data) {
		this.viewName = viewName;
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		PageView view = (PageView) o;

		if (!data.equals(view.data)) {
			return false;
		}
		return viewName.equals(view.viewName);
	}

	@Override
	public int hashCode() {
		int result = data.hashCode();
		result = 31 * result + viewName.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "PageView{" + "data=" + data + ", viewName='" + viewName + '\'' + '}';
	}
}
