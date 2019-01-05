package controllers.forms;

import play.data.validation.Constraints;

public class GiveAccessRelativeForm {

    @Constraints.Required
    public Long relativeId;

    @Constraints.Required
    public Long recordId;
}
