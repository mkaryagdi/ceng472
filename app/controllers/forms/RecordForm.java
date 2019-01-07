package controllers.forms;

public class RecordForm {

    public String diagnostic;

    public PatientForm patient;

    public RecordForm() {
        this.patient = new PatientForm();
    }
}
