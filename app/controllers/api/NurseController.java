package controllers.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jwt.filter.Attrs;
import models.DoctorUser;
import models.NurseUser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class NurseController extends Controller {

    public Result fetch(Long id) {

        NurseUser verifiedUser = request().attrs().get(Attrs.VERIFIED_NURSE_USER);

        if (verifiedUser.getId().equals(id)) {

            ObjectNode response = (ObjectNode) Json.toJson(verifiedUser);
            response.set("doctors", getDocsAndPatients(verifiedUser.getDoctorList()));
            return ok(response);

        } else {
            return badRequest("You are not allowed!");
        }
    }

    private ArrayNode getDocsAndPatients(List<DoctorUser> docList) {
        ArrayNode docs = Json.newArray();
        for (DoctorUser doctor : docList) {
            docs.add(Json.newObject().put("name", doctor.getName()).put("surname", doctor.getSurname())
                    .put("major", doctor.getMajor()).put("gender", doctor.getGender())
                    .set("patients", doctor.getPatients()));
        }

        return docs;
    }
}
