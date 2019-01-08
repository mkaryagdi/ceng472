package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import controllers.forms.DoctorForm;
import controllers.forms.NurseForm;
import controllers.forms.PatientForm;
import controllers.forms.RelativeForm;
import generator.user.DoctorGenerator;
import generator.user.NurseGenerator;
import jwt.JwtHelper;
import models.*;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

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
        String password = generateRandomPassword();
        String username = body.name.substring(0, 1).toUpperCase() + body.surname;
        try {
            doctor = doctorGenerator.generate(username,
                    password,
                    body.name,
                    body.surname,
                    body.major,
                    body.birthYear,
                    body.gender);
        } catch (Exception e) {
            return badRequest("doctor generation failed");
        }

        ObjectNode result = (ObjectNode) Json.toJson(doctor);
        result.put("username", username);
        result.put("password", password);
        return created(result);
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
        String username = body.name.substring(0, 1).toUpperCase() + body.surname;
        String password = generateRandomPassword();
        try {
            nurse = nurseGenerator.generate(username,
                    password,
                    body.name,
                    body.surname,
                    body.major,
                    body.gender,
                    null);
        } catch (Exception e) {
            return badRequest("nurse generation failed");
        }

        ObjectNode result = (ObjectNode) Json.toJson(nurse);
        result.put("username", username);
        result.put("password", password);
        return created(result);
    }

    public Result createRelative() throws Exception {

        Form<RelativeForm> form = formFactory.form(RelativeForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

        RelativeForm body = form.get();
        Logger.debug("Generating relative user.");
        String username = body.name.substring(0, 1).toUpperCase() + body.surname;
        String password = generateRandomPassword();
        RelativeUser relative = new RelativeUser(
                null,
                username,
                password,
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

        ObjectNode result = (ObjectNode) Json.toJson(relative);
        result.put("username", username);
        result.put("password", password);
        return created(Json.toJson(relative));
    }

    static public String generateRandomPassword() {
            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 8) { // length of the random string.
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            String saltStr = salt.toString();
            return saltStr;
        }

}
