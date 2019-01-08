package controllers.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jwt.filter.Attrs;
import models.Record;
import models.RelativeUser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class RelativeController extends Controller {

    public Result fetch(Long id) {

        RelativeUser verifiedUser = request().attrs().get(Attrs.VERIFIED_RELATIVE_USER);

        if (verifiedUser.getId().equals(id)) {
            ObjectNode result = (ObjectNode) Json.toJson(verifiedUser);
            ArrayNode patientsRecords = Json.newArray();
            for (int i = 0; i < verifiedUser.getPatientsRecords().size(); i++) {
                Record record = verifiedUser.getPatientsRecords().get(i);
                patientsRecords.add(Json.newObject().put("diagnostic", record.getDiagnostic())
                        .put("patientName", record.getPatientUser().getName())
                        .put("patientSurname", record.getPatientUser().getSurname())
                        .put("patientBirthYear", record.getPatientUser().getBirthYear())
                        .put("patientGender", record.getPatientUser().getGender())
                        .put("doctorName", record.getDoctorUser().getName())
                        .put("doctorMajor", record.getDoctorUser().getMajor()));
            }
            result.set("patientsRecords", patientsRecords);
            return ok(result);
        } else {
            return badRequest("You are not allowed!");
        }
    }
}
