package controllers.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import controllers.forms.DiagnosticForm;
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

    private ArrayNode getNurses(DoctorUser user) {
        ArrayNode array = Json.newArray();
        ArrayNode doctorNurse = Json.newArray();
        ArrayNode outherArray = Json.newArray();

        for (NurseUser nurse: NurseUser.finder.all()) {
            if (nurse.getDoctorList().contains(user)){
                doctorNurse.add(Json.toJson(nurse));
            } else {
                array.add(Json.toJson(nurse));
            }
        }
        outherArray.add(array);
        outherArray.add(doctorNurse);
        return outherArray;
    }

    public Result fetch(Long id) {

        DoctorUser verifiedUser = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        if (verifiedUser.getId().equals(id)) {

            ObjectNode doctorNode = (ObjectNode) Json.toJson(verifiedUser);
            doctorNode.set("patients", verifiedUser.getPatients());
            ArrayNode arrayNode = getNurses(verifiedUser);
            doctorNode.set("nurses", arrayNode.get(0));
            doctorNode.set("docNurses", arrayNode.get(1));
            return ok(doctorNode);

        } else {
            return badRequest("You are not allowed!");
        }
    }

    public Result createRecord() {

        DoctorUser doctor = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        Form<RecordForm> form = formFactory.form(RecordForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

//        if (PatientUser.finder.query().where().eq("username", body.username).findCount() != 0)
//            return badRequest("username already exists."); //TODO

        RecordForm body = form.get();
        PatientUser patient;
        Logger.debug("creating patient...");
        String username = body.patient.name.substring(0, 1).toUpperCase() + body.patient.surname;
        String password = AdminController.generateRandomPassword();
        try {
            patient = patientGenerator.generate(username,
                    password,
                    body.patient.name,
                    body.patient.surname,
                    body.patient.birthYear,
                    body.patient.address,
                    body.patient.gender,
                    doctor);
        } catch (Exception e) {
            return badRequest("Patient generation failed.");
        }

        Record record = new Record(body.diagnostic, patient, doctor);
        record.save();
        doctor.save();
        ObjectNode result = (ObjectNode) Json.toJson(record);
        result.put("password", password);
        result.put("username", username);

        return ok(result);
    }

    public Result addRecord(Long id) {

        DoctorUser doctor = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        PatientUser patient = PatientUser.finder.byId(id);

        if (!patient.getDoctorList().contains(doctor)) {
            return badRequest("You are not allowed!");
        }

        Form<DiagnosticForm> form = formFactory.form(DiagnosticForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

        DiagnosticForm body = form.get();

        if (patient == null) {
            return notFound("Patient does not found!");
        }

        Record record = new Record(body.diagnostic, patient, doctor);
        record.save();

        return ok(Json.toJson(record));
    }

    public Result assignNurse(Long id) {

        DoctorUser doctor = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        NurseUser nurse = NurseUser.finder.byId(id);

        if (nurse == null) {
            return notFound("Nurse does not found!");
        }

        if (nurse.getDoctorList() != null && nurse.getDoctorList().contains(doctor)) {
            return badRequest("Nurse is already assigned to you.");
        }

        nurse.addDoctorList(doctor);
        nurse.save();

        return ok(doctor.getNurse(nurse));
    }

}
