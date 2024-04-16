import java.util.Objects;

public class Movie {

	final String id;
	final String name;

	public Movie(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Movie movie = (Movie) o;
		return Objects.equals(id, movie.id) && Objects.equals(name, movie.name);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(id);
		result = 31 * result + Objects.hashCode(name);
		return result;
	}
}
