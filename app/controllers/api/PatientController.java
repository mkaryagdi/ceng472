package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.forms.GiveAccessDoctorForm;
import controllers.forms.GiveAccessRelativeForm;
import controllers.forms.RelativeForm;
import jwt.JwtHelper;
import jwt.filter.Attrs;
import models.DoctorUser;
import models.PatientUser;
import models.Record;
import models.RelativeUser;
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
//            patientNode.set("records", verifiedUser.getRecords());
//            patientNode.set("doctors", verifiedUser.getDoctors());
//            patientNode.set("relatives", verifiedUser.getRelatives());
            return ok(patientNode);

        } else {
            return badRequest("You are not allowed!");
        }
    }

    public Result giveAccessToDoctor() {

        PatientUser patientUser = request().attrs().get(Attrs.VERIFIED_PATIENT_USER);

        Form<GiveAccessDoctorForm> form = formFactory.form(GiveAccessDoctorForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

        GiveAccessDoctorForm body = form.get();

        DoctorUser doctorUser = DoctorUser.finder.byId(body.doctorId);

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

    public Result giveAccessToRelative() {

        PatientUser patientUser = request().attrs().get(Attrs.VERIFIED_PATIENT_USER);

        Form<GiveAccessRelativeForm> form = formFactory.form(GiveAccessRelativeForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

        GiveAccessRelativeForm body = form.get();

        RelativeUser relativeUser = RelativeUser.finder.byId(body.relativeId);

        if (relativeUser == null) {
            return notFound("relative is not found");
        }

        if (body.recordId == null) {
            return badRequest("record id is null");
        }

        Record recordToSet = null;
        for (Record record: patientUser.getRecordList()) {
            if (record.getId().equals(body.recordId)) {
                recordToSet = record;
                break;
            }
        }

        if (recordToSet == null) {
            return badRequest("user does not have such a record");
        }

        relativeUser.getPatientsRecords().add(recordToSet);
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
        RelativeUser relativeUser = new RelativeUser(null, body.name.substring(1) + body.surname, password, body.name, body.surname, patientUser, body.phoneNumber);

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