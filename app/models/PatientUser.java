package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
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

    @ManyToMany(cascade = CascadeType.ALL)
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
        this.doctorList = new ArrayList<>();
        this.doctorList.add(doctor);
        this.relativeList = new ArrayList<>();
        this.recordList = new ArrayList<>();
    }

    @JsonIgnore
    public List<DoctorUser> getDoctorList() {
        return doctorList;
    }

    public PatientUser setToken(String token) {
        this.token = token;
        return this;
    }

    public PatientUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public PatientUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public PatientUser setName(String name) {
        this.name = name;
        return this;
    }

    public PatientUser setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public PatientUser setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
        return this;
    }

    public PatientUser setAddress(String address) {
        this.address = address;
        return this;
    }

    public PatientUser setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setDoctorList(List<DoctorUser> doctorList) {
        this.doctorList = doctorList;
    }

    @JsonIgnore
    public List<RelativeUser> getRelativeList() {
        return relativeList;
    }

    public void setRelativeList(List<RelativeUser> relativeList) {
        this.relativeList = relativeList;
    }

    @JsonIgnore
    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public void addRecord(Record record) { this.recordList.add(record); }

    public void addRelative(RelativeUser relative) { this.relativeList.add(relative); }

    public void addDoctor(DoctorUser doctor) { this.doctorList.add(doctor); }

    public void removeDoctor(DoctorUser doctor) { this.doctorList.remove(doctor); }

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

//    public ArrayNode getRecords() {
//        ArrayNode array = Json.newArray();
//        for(Record record: this.recordList) {
//            array.add(Json.toJson(record));
//        }
//        return array;
//    }
//
//    public ArrayNode getRelatives() {
//        ArrayNode array = Json.newArray();
//        for(RelativeUser relative: this.relativeList) {
//            array.add(Json.toJson(relative));
//        }
//        return array;
//    }
//
//    public ObjectNode getDoctor(DoctorUser doctorUser) {
//
//        ObjectNode doctorNode = (ObjectNode) Json.toJson(doctorUser);
//        doctorNode.remove("username");
//        doctorNode.remove("password");
//        doctorNode.remove("token");
//        return doctorNode;
//    }
//
//    public ArrayNode getDoctors() {
//        ArrayNode array = Json.newArray();
//        for (DoctorUser doctor: this.doctorList) {
//            array.add(getDoctor(doctor));
//        }
//        return array;
//    }

}
