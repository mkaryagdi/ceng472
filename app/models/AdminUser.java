package models;

import io.ebean.Finder;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AdminUser extends User {

    @Id
    private Long id;

    public static final Finder<Long, AdminUser> finder = new Finder<>(AdminUser.class);

    public AdminUser(String token, String email, String password) {
        super(token, email, password);
    }

}
