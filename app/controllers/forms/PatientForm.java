package controllers.forms;

import play.data.validation.Constraints.Required;

public class PatientForm {

    @Required
    public String name;

    @Required
    public String surname;

    public Long phoneNumber;

    //public String birthDate;

    public String address;

    @Required
    public String username;
}
