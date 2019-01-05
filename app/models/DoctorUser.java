package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DoctorUser extends Model {

    @Id
    private Long id;

    @Column(length = 2048)
    private String token;

    private String username;

    private String password;

    private String name;

    private String surname;

    private String major;

    private Integer birthYear;

    private String gender;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PatientUser> patientList;

    public static final Finder<Long, DoctorUser> finder = new Finder<>(DoctorUser.class);

    public DoctorUser(String token, String username, String password, String name, String surname, String major, Integer birthYear, String gender) {
        this.token = token;
        this.username = username;
        this.password = password;
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

    public Long getId() {
        return id;
    }

    public DoctorUser setToken(String token) {

        this.token = token;
        return this;
    }

    public DoctorUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public DoctorUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public DoctorUser setName(String name) {
        this.name = name;
        return this;
    }

    public DoctorUser setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public DoctorUser setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
        return this;
    }

    public DoctorUser setGender(String gender) {
        this.gender = gender;
        return this;
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

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public String getGender() {
        return gender;
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

    public void addPatient(PatientUser patientUser) {
        this.patientList.add(patientUser);
    }
}
