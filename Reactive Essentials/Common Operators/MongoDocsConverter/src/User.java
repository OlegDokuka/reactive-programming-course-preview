import java.time.LocalDateTime;
import java.util.UUID;

public class User {

  final UUID id;
  final String login;
  final String password;
  final LocalDateTime lastViewed;
  final LocalDateTime registered;

  public User(UUID id,
      String login,
      String password,
      LocalDateTime lastViewed,
      LocalDateTime registered) {
    this.id = id;
    this.login = login;
    this.password = password;
    this.lastViewed = lastViewed;
    this.registered = registered;
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
    if (!login.equals(user.login)) {
      return false;
    }
    if (!password.equals(user.password)) {
      return false;
    }
    if (!lastViewed.equals(user.lastViewed)) {
      return false;
    }
    return registered.equals(user.registered);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + login.hashCode();
    result = 31 * result + password.hashCode();
    result = 31 * result + lastViewed.hashCode();
    result = 31 * result + registered.hashCode();
    return result;
  }
}
