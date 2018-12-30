package models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name="usr")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class User extends Model {

    @Id
    private Long id;

    private String token;

    private String email;

    private String password;

    public static final Finder<Long, User> finder = new Finder<>(User.class);

    public User(String token, String email, String password) {
        this.token = token;
        setEmail(email);
        setPassword(password);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
