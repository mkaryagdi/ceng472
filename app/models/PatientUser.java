package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class PatientUser extends Model {

    @Id
    private Long id;

    @Column(length = 2048)
    private String token;

    private String username;

    private String password;

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
        this.token = token;
        this.username = username;
        this.password = password;
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

    public void addRecord(Record record) { this.recordList.add(record); }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }
}
