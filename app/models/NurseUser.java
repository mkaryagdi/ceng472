package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Finder;
import io.ebean.Model;
import play.libs.Json;

import javax.persistence.*;

@Entity
public class NurseUser extends Model {

    @Id
    private Long id;

    @Column(length = 2048)
    private String token;

    private String username;

    private String password;

    private String name;

    private String surname;

    private String major;

    private String gender;

    @ManyToOne(cascade = CascadeType.ALL)
    private DoctorUser doctor;

    public static final Finder<Long, NurseUser> finder = new Finder<>(NurseUser.class);

    public NurseUser(String token, String username, String password, String name, String surname, String gender, String major, DoctorUser doctor) {
        this.token = token;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.major = major;
        this.doctor = doctor;
    }

    public String getMajor() {
        return major;
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

    public Long getId() {

        return id;
    }

    public NurseUser setToken(String token) {

        this.token = token;
        return this;
    }

    public NurseUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public NurseUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public NurseUser setName(String name) {
        this.name = name;
        return this;
    }

    public NurseUser setSurname(String surname) {
        this.surname = surname;
        return this;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
