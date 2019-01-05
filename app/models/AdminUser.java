package models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AdminUser extends Model {

    @Id
    private Long id;

    @Column(length = 2048)
    private String token;

    private String username;

    private String password;

    public static final Finder<Long, AdminUser> finder = new Finder<>(AdminUser.class);

    public AdminUser(String token, String username, String password) {
        this.token = token;
        this.username = username;
        this.password = password;
    }

}
