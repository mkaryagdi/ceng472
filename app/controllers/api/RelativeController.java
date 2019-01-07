package controllers.api;

import jwt.filter.Attrs;
import models.RelativeUser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class RelativeController extends Controller {

    public Result fetch(Long id) {

        RelativeUser verifiedUser = request().attrs().get(Attrs.VERIFIED_RELATIVE_USER);

        if (verifiedUser.getId().equals(id)) {
            return ok(Json.toJson(verifiedUser));
        } else {
            return badRequest("You are not allowed!");

        }
    }
}
