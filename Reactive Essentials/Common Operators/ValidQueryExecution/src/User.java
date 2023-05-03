import java.time.LocalDate;

public class User {
    final String email;
    final String nickname;
    final String password;
    final LocalDate birthday;

    public User(String email, String nickname, String password, LocalDate birthday) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.birthday = birthday;
    }
}
