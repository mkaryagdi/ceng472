package controllers.forms;

import play.data.validation.Constraints.Required;

public class UserSignInForm {

    @Required
    public String username;

    @Required
    public String password;

}
