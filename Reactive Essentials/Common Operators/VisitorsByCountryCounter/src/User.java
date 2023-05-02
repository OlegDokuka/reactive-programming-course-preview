public class User {
  final String id;
  final String country;

  public User(String id, String country) {
    this.id = id;
    this.country = country;
  }

  public String getId() {
    return id;
  }

  public String getCountry() {
    return country;
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", country='" + country + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;

    if (!id.equals(user.id)) {
      return false;
    }
    return country.equals(user.country);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + country.hashCode();
    return result;
  }
}
