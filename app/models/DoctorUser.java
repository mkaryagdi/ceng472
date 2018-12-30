package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@DiscriminatorValue("doctor")
public class DoctorUser extends User {

    private String major;

    private String email;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PatientUser> patientList;

    public static final Finder<Long, DoctorUser> finder = new Finder<>(DoctorUser.class);

    public DoctorUser(String accessToken, String name, String surname, String major, String email) {
        super(accessToken, name, surname);
        setMajor(major);
        setEmail(email);
        this.patientList = new ArrayList<>();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public List<PatientUser> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<PatientUser> patientList) {
        this.patientList = patientList;
    }
}
