package controllers.api;

import com.google.inject.Inject;
import controllers.forms.PatientForm;
import jwt.JwtHelper;
import jwt.filter.Attrs;
import models.DoctorUser;
import models.PatientUser;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class DoctorUserController extends Controller {

    private FormFactory formFactory;
    private JwtHelper jwtHelper;

    @Inject
    public DoctorUserController(FormFactory formFactory, JwtHelper jwtHelper) {
        this.formFactory = formFactory;
        this.jwtHelper = jwtHelper;
    }

    public Result fetch(Long id) {

        DoctorUser verifiedUser = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        if (verifiedUser.getId().equals(id)) {
            return ok(Json.toJson(verifiedUser));
        } else {
            return badRequest("You are not allowed!");
        }
    }

    public Result createPatient() {

        DoctorUser doctor = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        Form<PatientForm> form = formFactory.form(PatientForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest("form has errors.");
        }

        PatientForm body = form.get();

        if (PatientUser.finder.query().where().eq("username", body.username).findCount() != 0)
            return badRequest("username already exists.");

        Logger.debug("creating patient...");
        String password = new Random(10).toString(); // TODO: make it secure
        PatientUser patient = new PatientUser(null, body.username, password, body.name, body.surname,
                body.phoneNumber, body.address, doctor);

        try {
            patient.setToken(jwtHelper.getSignedToken(patient.getId(), false, false, false, true, false));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        doctor.addPatient(patient);
        patient.save();
        doctor.save();

        return created(Json.toJson(patient));
    }

//    public Result createRecord() {
//
//        DoctorUser doctor = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);
//
//        Form<RecordForm> form = formFactory.form(RecordForm.class).bind(request().body().asJson());
//
//        if (form.hasErrors()) {
//            return badRequest("form has errors.");
//        }
//
//        RecordForm body = form.get();
//    }
}
