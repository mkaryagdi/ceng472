package controllers.forms;

import play.data.validation.Constraints;

public class GiveAccessDoctorForm {

    @Constraints.Required
    public Long doctorId;
}
