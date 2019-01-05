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

    @OneToOne
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
}
