package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Entity
public class NurseUser extends Model {

    @Id
    private Long id;

    @Column(length = 2048)
    private String token;

    private String username;

    private byte[] password;

    private String name;

    private String surname;

    private String major;

    private String gender;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<DoctorUser> doctorList;

    public static final Finder<Long, NurseUser> finder = new Finder<>(NurseUser.class);

    public NurseUser(String token, String username, String password, String name, String surname, String gender, String major, DoctorUser doctor) {
        this.token = token;
        this.username = username;        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encPassword = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        this.password = encPassword;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.major = major;
        this.doctorList = new ArrayList<>();
        if (doctor != null) {
            this.doctorList.add(doctor);
        }
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

    public byte[] getPassword() {
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
    public List<DoctorUser> getDoctorList() {
        return doctorList;
    }

    public NurseUser addDoctorList(DoctorUser doctor) {
        this.doctorList.add(doctor);
        return this;
    }

    public static Finder<Long, NurseUser> getFinder() {
        return finder;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
