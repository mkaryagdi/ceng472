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

    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private PatientUser patient;

    private Long phoneNumber;

    public static final Finder<Long, RelativeUser> finder = new Finder<>(RelativeUser.class);

    public RelativeUser (String token, String name, String surname, PatientUser patient, Long phoneNumber){
        super(token, name, surname);
        setPatient(patient);
        setPhoneNumber(phoneNumber);
    }

    @Override
    public Long getId() {
        return id;
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
