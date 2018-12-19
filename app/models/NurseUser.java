package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@DiscriminatorValue("nurse")
public class NurseUser extends User {

    private String major;

    @NotNull
    private String email;

    @OneToMany
    private List<PatientUser> patientList;

    @OneToOne
    private DoctorUser doctor;

    public static final Finder<Long, NurseUser> finder = new Finder<>(NurseUser.class);

    public NurseUser(String name, String surname, String major, String email, DoctorUser doctor) {
        super(name, surname);
        setMajor(major);
        setEmail(email);
        setDoctor(doctor);
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
