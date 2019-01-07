package controllers.forms;

import play.data.validation.Constraints;

public class NurseForm {

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String surname;

    @Constraints.Required
    public String major;

    public String gender;
}
