package models;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity
public class Record extends Model {

    @Id
    private Long id;

    @NotNull
    @Size(max = 256)
    private String diagnostic;

    @ManyToOne(cascade = CascadeType.ALL)
    private PatientUser patientUser;

    public Record(String diagnostic, PatientUser patientUser) {
        this.diagnostic = diagnostic;
        this.patientUser = patientUser;
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
