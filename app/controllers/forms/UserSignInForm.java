package controllers.forms;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

public class UserSignInForm {

    @Required
    @Email
    public String email;

    @Required
    public String password;

}
