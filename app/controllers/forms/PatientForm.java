package controllers.forms;

import org.joda.time.DateTime;
import play.data.validation.Constraints.Required;

public class PatientForm {

    @Required
    private String name;

    @Required
    private String surname;

    private Long phoneNumber;

    private DateTime birthDate;

    private String address;

    @Required
    public String username;

    @Required
    public String password;
}
