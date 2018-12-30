package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("relative")
public class RelativeUser extends User {

    private String name;

    private String surname;

    @ManyToOne(cascade = CascadeType.ALL)
    private PatientUser patient;

    private Long phoneNumber;

    public static final Finder<Long, RelativeUser> finder = new Finder<>(RelativeUser.class);

    public RelativeUser (String token, String username, String password, String name, String surname, PatientUser patient, Long phoneNumber){
        super(token, username, password);
        this.name = name;
        this.surname = surname;
        setPatient(patient);
        setPhoneNumber(phoneNumber);
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    @JsonIgnore
    public PatientUser getPatient() {
        return patient;
    }

    public void setPatient(PatientUser patient) {
        this.patient = patient;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
