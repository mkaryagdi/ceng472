package controllers.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.forms.RelativeForm;
import generator.user.RelativeGenerator;
import jwt.JwtHelper;
import jwt.filter.Attrs;
import models.DoctorUser;
import models.PatientUser;
import models.Record;
import models.RelativeUser;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.Random;

public class PatientController extends Controller {

    private FormFactory formFactory;
    private JwtHelper jwtHelper;
    private RelativeGenerator relativeGenerator;

    @Inject
    public PatientController(FormFactory formFactory, JwtHelper jwtHelper, RelativeGenerator relativeGenerator) {
        this.formFactory = formFactory;
        this.jwtHelper = jwtHelper;
        this.relativeGenerator = relativeGenerator;
    }

    public Result fetch(Long id) {

        PatientUser verifiedUser = request().attrs().get(Attrs.VERIFIED_PATIENT_USER);

        if (verifiedUser.getId().equals(id)) {

            ObjectNode patientNode = (ObjectNode) Json.toJson(verifiedUser);
            patientNode.set("selfDoctors", verifiedUser.getDoctors());
            patientNode.set("records", verifiedUser.getRecords());
            patientNode.set("relatives", verifiedUser.getRelatives());

            ArrayNode array = Json.newArray();
            for (DoctorUser doctor: DoctorUser.finder.all()) {
                if (!verifiedUser.getDoctorList().contains(doctor)) {
                    array.add(verifiedUser.getDoctor(doctor));
                }
            }
            patientNode.set("doctors", array);

            return ok(patientNode);

        } else {
            return badRequest("You are not allowed!");
        }
    }

    public Result giveAccessToDoctor(Long id) {

        PatientUser patientUser = request().attrs().get(Attrs.VERIFIED_PATIENT_USER);

        DoctorUser doctorUser = DoctorUser.finder.byId(id);

        if (doctorUser == null) {
            return notFound("doctor is not found");
        }

        if (patientUser.getDoctorList().contains(doctorUser)) {
            return badRequest("already authorized");
        }

        patientUser.getDoctorList().add(doctorUser); // cascade?? which one to save
        doctorUser.addPatient(patientUser);
        doctorUser.save();

        return ok();
    }

    public Result giveAccessToRelative(Long relativeId, Long recordId) {

        PatientUser patientUser = request().attrs().get(Attrs.VERIFIED_PATIENT_USER);

        RelativeUser relativeUser = RelativeUser.finder.byId(relativeId);

        if (relativeUser == null) {
            return notFound("relative is not found");
        }

        if (!patientUser.getRelativeList().contains(relativeUser)) {
            return badRequest("relative is not yours");
        }

        if (recordId == null) {
            return badRequest("record id is null");
        }

        Record record = null;
        for (Record r: patientUser.getRecordList()) {
            if (r.getId().equals(recordId)) {
                record = r;
                break;
            }
        }

        if (record == null) {
            return badRequest("user does not have such a record");
        }

        if (relativeUser.getPatientsRecords().contains(record)) {
            return badRequest("relative is already authorized for this record");
        }

        relativeUser.getPatientsRecords().add(record);
        relativeUser.save();

        return ok();
    }

    public Result createRelative() {

        PatientUser patientUser = request().attrs().get(Attrs.VERIFIED_PATIENT_USER);

        Form<RelativeForm> form = formFactory.form(RelativeForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

        RelativeForm body = form.get();
        RelativeUser relative;
        Logger.debug("creating relative...");
        String password = AdminController.generateRandomPassword();
        String username = body.name.substring(0, 1).toUpperCase() + body.surname;
        try {
            relative = relativeGenerator.generate(username,
                    password,
                    body.name,
                    body.surname,
                    body.phoneNumber,
                    null);
        } catch (Exception e) {
            return badRequest("relative generation failed");
        }

        patientUser.addRelative(relative);
        patientUser.save();

        ObjectNode result = (ObjectNode) Json.toJson(relative);
        result.put("password", password);
        result.put("username", username);
        return created(result);
    }
}