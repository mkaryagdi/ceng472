package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("nurse")
public class NurseUser extends User {

    private String name;

    private String surname;

    private String major;

    @ManyToOne(cascade = CascadeType.ALL)
    private DoctorUser doctor;

    public static final Finder<Long, NurseUser> finder = new Finder<>(NurseUser.class);

    public NurseUser(String token, String name, String surname, String major, String username, String password, DoctorUser doctor) {
        super(token, username, password);
        this.name = name;
        this.surname = surname;
        this.major = major;
        setDoctor(doctor);
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @JsonIgnore
    public DoctorUser getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorUser doctor) {
        this.doctor = doctor;
    }
}
