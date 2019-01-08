package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Finder;
import io.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PatientUser extends Model {

    @Id
    private Long id;

    @Column(length = 2048)
    private String token;

    private String username;

    private byte[] password;

    private String name;

    private String surname;

    private Integer birthYear;

    private String address;

    private String gender;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<DoctorUser> doctorList;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<RelativeUser> relativeList;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Record> recordList;

    public static final Finder<Long, PatientUser> finder = new Finder<>(PatientUser.class);

    public PatientUser(String token, String username, String password, String name, String surname, Integer birthYear, String address, String gender, DoctorUser doctor) {
        this.token = token;
        this.username = username;
        MessageDigest digest = null;
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
        this.birthYear = birthYear;
        this.address = address;
        this.gender = gender;
        this.doctorList = new ArrayList<>();
        if (doctor != null)
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

    public byte[] getPassword() {
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

    @JsonIgnore
    public ArrayNode getRecords() {
        ArrayNode array = Json.newArray();
        for(Record record: this.recordList) {
            array.add(getRecord(record));
        }
        return array;
    }

    private JsonNode getRecord(Record record) {
        return Json.newObject().put("diagnostic", record.getDiagnostic())
                .put("id", record.getId())
                .set("doctor", getDoctor(record.getDoctorUser()));
    }

    @JsonIgnore
    public ArrayNode getRelatives() {
        ArrayNode array = Json.newArray();
        for(RelativeUser relative: this.relativeList) {
            array.add(getRelative(relative));
        }
        return array;
    }

    private JsonNode getRelative(RelativeUser relative) {
        return Json.newObject().put("name", relative.getName())
                .put("surname", relative.getSurname())
                .put("id", relative.getId());
    }

    @JsonIgnore
    public ArrayNode getDoctors() {
        ArrayNode array = Json.newArray();
        for (DoctorUser doctor: this.doctorList) {
            array.add(getDoctor(doctor));
        }
        return array;
    }

    public JsonNode getDoctor(DoctorUser doctorUser) {
        ObjectNode doctorNode = (ObjectNode) Json.toJson(doctorUser);
        doctorNode.remove("username");
        doctorNode.remove("password");
        doctorNode.remove("token");
        doctorNode.remove("patients");
        return doctorNode;
    }

}
