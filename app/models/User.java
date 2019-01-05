package models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;


@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User extends Model {

    @Id
    private Long id;

    @Column(length = 2048)
    private String token;

    private String username;

    private String password;

    public static final Finder<Long, User> finder = new Finder<>(User.class);

    public User(String token, String username, String password) {
        this.token = token;
        setUsername(username);
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
