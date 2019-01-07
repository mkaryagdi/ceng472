package models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RelativeUser extends Model {

    @Id
    private Long id;

    @Column(length = 2048)
    private String token;

    private String username;

    private String password;

    private String name;

    private String surname;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Record> patientsRecords;

    private Double phoneNumber;

    public static final Finder<Long, RelativeUser> finder = new Finder<>(RelativeUser.class);

    public RelativeUser (String token, String username, String password, String name, String surname, Double phoneNumber){
        this.token = token;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.patientsRecords = new ArrayList<>();
        setPhoneNumber(phoneNumber);
    }

    public Double getPhoneNumber() {
        return phoneNumber;
    }

    public RelativeUser setToken(String token) {
        this.token = token;
        return this;
    }

    public RelativeUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public RelativeUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public RelativeUser setName(String name) {
        this.name = name;
        return this;
    }

    public RelativeUser setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public RelativeUser setPatientsRecords(List<Record> patientsRecords) {
        this.patientsRecords = patientsRecords;
        return this;
    }
    public RelativeUser addRecords(Record record) {
        this.patientsRecords.add(record);
        return this;
    }

    public void setPhoneNumber(Double phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Record> getPatientsRecords() {
        return patientsRecords;
    }

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
}
