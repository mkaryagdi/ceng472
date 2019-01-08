package models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
public class AdminUser extends Model {

    @Id
    private Long id;

    @Column(length = 2048)
    private String token;

    private String username;

    private byte[] password;

    public static final Finder<Long, AdminUser> finder = new Finder<>(AdminUser.class);

    public AdminUser(String token, String username, String password) {
        this.token = token;
        this.username = username;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encPassword = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        this.password = encPassword;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getPassword() {
        return password;
    }

    public AdminUser setToken(String token) {

        this.token = token;
        return this;
    }

    public AdminUser setUsername(String username) {
        this.username = username;
        return this;
    }
}
