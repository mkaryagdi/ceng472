package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@DiscriminatorValue("doctor")
public class DoctorUser extends User {

    private String name;

    private String surname;

    private String major;

    private Integer birthYear;

    private String gender;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PatientUser> patientList;

    public static final Finder<Long, DoctorUser> finder = new Finder<>(DoctorUser.class);

    public DoctorUser(String token, String username, String password, String name, String surname, String major, Integer birthYear, String gender) {
        super(token, username, password);
        this.name = name;
        this.surname = surname;
        this.major = major;
        this.birthYear = birthYear;
        this.gender = gender;
        this.patientList = new ArrayList<>();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @JsonIgnore
    public List<PatientUser> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<PatientUser> patientList) {
        this.patientList = patientList;
    }

    public void addPatient(PatientUser patientUser) {
        this.patientList.add(patientUser);
    }
}
