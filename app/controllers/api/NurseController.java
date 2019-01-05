package controllers.api;

import jwt.filter.Attrs;
import models.DoctorUser;
import models.NurseUser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class NurseController extends Controller {

    public Result fetch(Long id) {

        NurseUser verifiedUser = request().attrs().get(Attrs.VERIFIED_NURSE_USER);

        if (verifiedUser.getId().equals(id)) {
            return ok(Json.toJson(verifiedUser));
        } else {
            return badRequest("You are not allowed!");
        }
    }
}
