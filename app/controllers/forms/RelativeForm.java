package controllers.forms;

import play.data.validation.Constraints;

public class RelativeForm {


    @Constraints.Required
    public String name;

    @Constraints.Required
    public String surname;

    public Long phoneNumber;
}
