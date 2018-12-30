package controllers.forms;

import play.data.validation.Constraints;

public class GiveAccessForm {

    @Constraints.Required
    public String type;

    @Constraints.Required
    public Long id;

    public Long recordId;
}
