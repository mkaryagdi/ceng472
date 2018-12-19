package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.DiscriminatorValue;
import javax.persistence.OneToMany;
import java.util.List;

@DiscriminatorValue("doctor")
public class DoctorUser extends User {

    private String major;

    private String email;

    @OneToMany
    private List<PatientUser> patientList;

    public DoctorUser(String name, String surname, String major, String email, List<PatientUser> patientList) {
        super(name, surname);
        setMajor(major);
        setEmail(email);
        setPatientList(patientList);
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
