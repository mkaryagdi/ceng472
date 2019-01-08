package controllers.api;

import com.google.inject.Inject;
import controllers.forms.*;
import generator.user.DoctorGenerator;
import generator.user.NurseGenerator;
import generator.user.PatientGenerator;
import generator.user.RelativeGenerator;
import jwt.JwtHelper;
import models.*;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.print.Doc;
import java.nio.charset.Charset;
import java.util.Random;

public class AdminController extends Controller {

    private FormFactory formFactory;
    private JwtHelper jwtHelper;
    private DoctorGenerator doctorGenerator;
    private NurseGenerator nurseGenerator;

    @Inject
    public AdminController(FormFactory formFactory, JwtHelper jwtHelper,
                           DoctorGenerator doctorGenerator, NurseGenerator nurseGenerator) {
        this.formFactory = formFactory;
        this.jwtHelper = jwtHelper;
        this.doctorGenerator = doctorGenerator;
        this.nurseGenerator = nurseGenerator;
    }

    public Result createDoctor() {

        Form<DoctorForm> form = formFactory.form(DoctorForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

        DoctorForm body = form.get();
        DoctorUser doctor;
        Logger.debug("creating doctor...");
        try {
            doctor = doctorGenerator.generate(body.name.substring(0, 1).toUpperCase() + body.surname,
                    generateRandomPassword(),
                    body.name,
                    body.surname,
                    body.major,
                    body.birthYear,
                    body.gender);
        } catch (Exception e) {
            return badRequest("doctor generation failed");
        }

        return created(Json.toJson(doctor));
    }

    public Result createPatient() throws Exception {

        Form<PatientForm> form = formFactory.form(PatientForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

//        if (PatientUser.finder.query().where().eq("username", body.username).findCount() != 0)
//            return badRequest("username already exists."); //TODO

        PatientForm body = form.get();
        Logger.debug("Generating patient user.");
        PatientUser patient = new PatientUser(
                null,
                body.name.substring(0, 1).toUpperCase() + body.surname,
                generateRandomPassword(),
                body.name,
                body.surname,
                body.birthYear,
                body.address,
                body.gender,
                null);
        // since we need userId to generate token, first we should save bean.
        patient.save();

        try {
            patient.setToken(jwtHelper.getSignedToken(patient.getId(), false, false, false, true, false));
            patient.save();
        } catch (Exception e) {
            patient.delete();
            throw e;
        }

        return ok(Json.toJson(patient));
    }

    public Result createNurse() {

        Form<NurseForm> form = formFactory.form(NurseForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

        NurseForm body = form.get();
        NurseUser nurse;
        Logger.debug("creating nurse...");
        try {
            nurse = nurseGenerator.generate(body.name.substring(0, 1).toUpperCase() + body.surname,
                    generateRandomPassword(),
                    body.name,
                    body.surname,
                    body.major,
                    body.gender,
                    null);
        } catch (Exception e) {
            return badRequest("nurse generation failed");
        }

        return created(Json.toJson(nurse));
    }

    public Result createRelative() throws Exception {

        Form<RelativeForm> form = formFactory.form(RelativeForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

        RelativeForm body = form.get();
        Logger.debug("Generating relative user.");
        RelativeUser relative = new RelativeUser(
                null,
                body.name.substring(0, 1).toUpperCase() + body.surname,
                generateRandomPassword(),
                body.name,
                body.surname,
                body.phoneNumber);
        // since we need userId to generate token, first we should save bean.
        relative.save();

        try {
            relative.setToken(jwtHelper.getSignedToken(relative.getId(), false, false, false, false, true));
            relative.save();
        } catch (Exception e) {
            relative.delete();
            throw e;
        }

        return created(Json.toJson(relative));
    }

    private String generateRandomPassword() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

}
