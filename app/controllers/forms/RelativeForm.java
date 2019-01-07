package controllers.forms;

import play.data.validation.Constraints.Required;

public class RelativeForm {

    @Required
    public String name;

    @Required
    public String surname;

    public Double phoneNumber;
}
