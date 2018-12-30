package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
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

    @OneToMany(cascade = CascadeType.ALL)
    private List<PatientUser> patientList;

    @OneToOne(cascade = CascadeType.ALL)
    private DoctorUser doctor;

    public static final Finder<Long, NurseUser> finder = new Finder<>(NurseUser.class);

    public NurseUser(String token, String name, String surname, String major, String email, String password, DoctorUser doctor) {
        super(token, email, password);
        this.name = name;
        this.surname = surname;
        this.major = major;
        setDoctor(doctor);
        this.patientList = new ArrayList<>();
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

    @JsonIgnore
    public List<PatientUser> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<PatientUser> patientList) {
        this.patientList = patientList;
    }
}
