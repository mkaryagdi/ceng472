package controllers.api;

import jwt.filter.Attrs;
import models.DoctorUser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class DoctorUserController extends Controller {

    public Result fetch(Long id) {

        Long userId = request().attrs().get(Attrs.VERIFIED_JWT).getUserId();
        DoctorUser verifiedUser = DoctorUser.finder.byId(userId);

        if(verifiedUser.getId().equals(id)) {
            return ok(Json.toJson(verifiedUser));
        } else {
            return badRequest("You are not allowed!");
        }
    }

//    public Result fetchPatient(Long id) {
//
//        Long patientId = request().
//    }
}
