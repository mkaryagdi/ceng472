package controllers.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.forms.RelativeForm;
import jwt.JwtHelper;
import jwt.filter.Attrs;
import models.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Random;

public class PatientController extends Controller {

    private FormFactory formFactory;
    private JwtHelper jwtHelper;

    @Inject
    public PatientController(FormFactory formFactory, JwtHelper jwtHelper) {
        this.formFactory = formFactory;
        this.jwtHelper = jwtHelper;
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
        String password = generateRandomPassword();
        RelativeUser relativeUser = new RelativeUser(null, body.name.substring(1) + body.surname, password, body.name, body.surname, body.phoneNumber);

        try {
            relativeUser.setToken(jwtHelper.getSignedToken(relativeUser.getId(), false, false, false, false, true));
            patientUser.addRelative(relativeUser);
            patientUser.save();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return created(Json.toJson(relativeUser));
    }

    private String generateRandomPassword() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}