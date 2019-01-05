package controllers.api;

import com.google.inject.Inject;
import controllers.forms.PatientForm;
import controllers.forms.RecordForm;
import generator.user.PatientGenerator;
import jwt.JwtHelper;
import jwt.filter.Attrs;
import models.DoctorUser;
import models.NurseUser;
import models.PatientUser;
import models.Record;
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
    private PatientGenerator patientGenerator;

    @Inject
    public DoctorUserController(FormFactory formFactory, JwtHelper jwtHelper, PatientGenerator patientGenerator) {
        this.formFactory = formFactory;
        this.jwtHelper = jwtHelper;
        this.patientGenerator = patientGenerator;
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
        PatientUser patient = null;
        try {
            patient = patientGenerator.generate(body.username, password, body.name, body.surname, body.birthYear,
                    body.address, body.gender, doctor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        doctor.addPatient(patient);
        doctor.save();
        patient.save();

        return created(Json.toJson(patient));
    }

    public Result createRecord(Long id) {

        DoctorUser doctor = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        Form<RecordForm> form = formFactory.form(RecordForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest("form has errors.");
        }

        PatientUser patientUser = PatientUser.finder.byId(id);

        if(patientUser == null) {
            return notFound("patient cant be found");
        }

        RecordForm body = form.get();

        Record record = new Record(body.diagnostic, patientUser, doctor);

        record.save();
        return ok();
    }

    public Result addNurse(Long id) {

        DoctorUser doctor = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        NurseUser nurseUser = NurseUser.finder.byId(id);

        if(nurseUser == null) {
            return notFound("nurse cant be found");
        }
        nurseUser.setDoctor(doctor);
        nurseUser.save();
        return ok();
    }


}
