package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@DiscriminatorValue("patient")
public class PatientUser extends User {

    private String name;

    private String surname;

    private Integer birthYear;

    private String address;

    private String gender;

    @ManyToOne(cascade = CascadeType.ALL)
    private List<DoctorUser> doctorList;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RelativeUser> relativeList;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Record> recordList;

    public static final Finder<Long, PatientUser> finder = new Finder<>(PatientUser.class);

    public PatientUser(String token, String username, String password, String name, String surname, Integer birthYear, String address, String gender, DoctorUser doctor) {
        super(token, username, password);
        this.name = name;
        this.surname = surname;
        this.birthYear = birthYear;
        this.address = address;
        this.gender = gender;
        this.doctorList = Arrays.asList(doctor);
        this.relativeList = new ArrayList<>();
        this.recordList = new ArrayList<>();
    }

    public List<DoctorUser> getDoctorList() {
        return doctorList;
    }

    @JsonIgnore
    public void setDoctorList(List<DoctorUser> doctorList) {
        this.doctorList = doctorList;
    }

    public List<RelativeUser> getRelativeList() {
        return relativeList;
    }

    public void setRelativeList(List<RelativeUser> relativeList) {
        this.relativeList = relativeList;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }
}
