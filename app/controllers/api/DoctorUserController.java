package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
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

            ObjectNode doctorNode = (ObjectNode) Json.toJson(verifiedUser);
            doctorNode.set("patients", verifiedUser.getPatients());
            return ok(doctorNode);

        } else {
            return badRequest("You are not allowed!");
        }
    }

    public Result createRecord() {

        DoctorUser doctor = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        Form<RecordForm> form = formFactory.form(RecordForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest("form has errors.");
        }

//        if (PatientUser.finder.query().where().eq("username", body.username).findCount() != 0)
//            return badRequest("username already exists."); //TODO

        RecordForm body = form.get();
        PatientUser patient;
        Logger.debug("creating patient...");
        try {
            patient = patientGenerator.generate(body.patient.name.substring(0, 1) + body.patient.surname,
                    new Random(10).toString(),
                    body.patient.name,
                    body.patient.surname,
                    body.patient.birthYear,
                    body.patient.address,
                    body.patient.gender,
                    doctor);
        } catch (Exception e) {
            return badRequest("patient generation failed");
        }

        Record record = new Record(body.diagnostic, patient, doctor);
        doctor.save();

        return ok(Json.toJson(record));
    }

    public Result addRecord(Long id) {

        PatientUser patient = PatientUser.finder.byId(id);

        DoctorUser doctor = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        if (!patient.getDoctorList().contains(doctor)) {
            return badRequest("You are not allowed!");
        }

        Form<RecordForm> form = formFactory.form(RecordForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest("form has errors.");
        }

        RecordForm body = form.get();

        if (patient == null) {
            return notFound("patient does not found");
        }

        Record record = new Record(body.diagnostic, patient, doctor);
        doctor.save();

        return ok(Json.toJson(record));
    }

    public Result assignNurse(Long id) {

        DoctorUser doctor = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        NurseUser nurse = NurseUser.finder.byId(id);

        if (nurse == null) {
            return notFound("nurse does not found");
        }

        if (nurse.getDoctor() != null && nurse.getDoctor().getId() == doctor.getId()) {
            return badRequest("nurse is already assigned to you.");
        }

        if (nurse.getDoctor() != null && nurse.getDoctor().getId() != doctor.getId()) {
            return badRequest("nurse is already assigned to a doctor.");
        }

        nurse.setDoctor(doctor);
        nurse.save();

        return ok(doctor.getNurse(nurse));
    }

}
