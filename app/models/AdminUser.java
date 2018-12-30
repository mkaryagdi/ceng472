package models;

import io.ebean.Finder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@DiscriminatorValue("admin")
public class AdminUser extends User {


    public static final Finder<Long, AdminUser> finder = new Finder<>(AdminUser.class);

    public AdminUser(String token, String email, String password) {
        super(token, email, password);
    }

}
