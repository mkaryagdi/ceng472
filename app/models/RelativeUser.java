package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.OneToOne;

@DiscriminatorValue("relative")
public class RelativeUser extends User {

    private Long id;

    @OneToOne
    private PatientUser patient;

    private Long phoneNumber;

    public static final Finder<Long, RelativeUser> finder = new Finder<>(RelativeUser.class);

    public RelativeUser (String name, String surname, PatientUser patient, Long phoneNumber){
        super(name, surname);
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
