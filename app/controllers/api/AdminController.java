package controllers.api;

import com.google.inject.Inject;
import controllers.forms.NurseForm;
import controllers.forms.PatientForm;
import controllers.forms.RecordForm;
import controllers.forms.RelativeForm;
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

import java.nio.charset.Charset;
import java.util.Random;

public class AdminController extends Controller {

    private FormFactory formFactory;
    private JwtHelper jwtHelper;
    private PatientGenerator patientGenerator;
    private RelativeGenerator relativeGenerator;
    private NurseGenerator nurseGenerator;

    @Inject
    public AdminController(FormFactory formFactory, JwtHelper jwtHelper,
                           PatientGenerator patientGenerator, RelativeGenerator relativeGenerator,
                           NurseGenerator nurseGenerator) {
        this.formFactory = formFactory;
        this.jwtHelper = jwtHelper;
        this.patientGenerator = patientGenerator;
        this.relativeGenerator = relativeGenerator;
        this.nurseGenerator = nurseGenerator;
    }

    public Result createDoctor() {
        return null;
    }

    public Result createPatient() {

        Form<PatientForm> form = formFactory.form(PatientForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

//        if (PatientUser.finder.query().where().eq("username", body.username).findCount() != 0)
//            return badRequest("username already exists."); //TODO

        PatientForm body = form.get();
        PatientUser patient;
        Logger.debug("creating patient...");
        try {
            patient = patientGenerator.generate(body.name.substring(0, 1).toUpperCase() + body.surname,
                    generateRandomPassword(),
                    body.name,
                    body.surname,
                    body.birthYear,
                    body.address,
                    body.gender,
                    null);
        } catch (Exception e) {
            return badRequest("patient generation failed");
        }

        patient.save();

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

    public Result createRelative() {

        Form<RelativeForm> form = formFactory.form(RelativeForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

        RelativeForm body = form.get();
        RelativeUser relative;
        Logger.debug("creating relative...");
        try {
            relative = relativeGenerator.generate(body.name.substring(0, 1).toUpperCase() + body.surname,
                    generateRandomPassword(),
                    body.name,
                    body.surname,
                    body.phoneNumber,
                    null);
        } catch (Exception e) {
            return badRequest("relative generation failed");
        }

        return created(Json.toJson(relative));
    }

    private String generateRandomPassword() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

}
