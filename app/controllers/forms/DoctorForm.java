package controllers.forms;

import play.data.validation.Constraints;

public class DoctorForm {

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String surname;

    @Constraints.Required
    public String major;

    public Integer birthYear;

    public String gender;
}

