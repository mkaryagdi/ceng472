package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import org.joda.time.DateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.List;

@DiscriminatorValue("patient")
public class PatientUser extends User {

    @NotNull
    private Long phoneNumber;

    @NotNull
    private DateTime birthDate;

    private String address;

    @ManyToOne
    private List<DoctorUser> doctorList;

    public static final Finder<Long, PatientUser> finder = new Finder<>(PatientUser.class);

    public PatientUser(String name, String surname, Long phoneNumber, DateTime birthDate, String address, List<DoctorUser> doctorList) {
        super(name, surname);
        setPhoneNumber(phoneNumber);
        setBirthdate(birthDate);
        setAddress(address);
        setDoctorList(doctorList);
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<DoctorUser> getDoctorList() {
        return doctorList;
    }

    @JsonIgnore
    public void setDoctorList(List<DoctorUser> doctorList) {
        this.doctorList = doctorList;
    }

    public DateTime getBirthdate() {
        return birthDate;
    }

    public void setBirthdate(DateTime birthDate) {
        this.birthDate = birthDate;
    }
}
