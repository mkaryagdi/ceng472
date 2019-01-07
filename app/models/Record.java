package models;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Record extends Model {

    @Id
    private Long id;

    @NotNull
    @Size(max = 256)
    private String diagnostic;

    private DoctorUser doctorUser;

    @ManyToOne(cascade = CascadeType.ALL)
    private PatientUser patientUser;

    public Record(String diagnostic, PatientUser patientUser, DoctorUser doctorUser) {
        this.diagnostic = diagnostic;
        this.patientUser = patientUser;
        this.doctorUser = doctorUser;
        patientUser.addRecord(this);
    }

    public Long getId() {
        return id;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public PatientUser getPatientUser() {
        return patientUser;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public DoctorUser getDoctorUser() {
        return doctorUser;
    }

    public void setDoctorUser(DoctorUser doctorUser) {
        this.doctorUser = doctorUser;
    }

    public void setPatientUser(PatientUser patientUser) {
        this.patientUser = patientUser;
    }
}
