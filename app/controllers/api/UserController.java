package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import controllers.forms.UserSignInForm;
import handlers.ErrorHandler;
import jwt.JwtHelper;
import models.*;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class UserController extends Controller {

    private FormFactory formFactory;
    private JwtHelper jwtHelper;
    private ErrorHandler errorHandler;
    private Logger.ALogger logger = Logger.of("controllers.api.dashboard.AdminController");

    @Inject
    public UserController(FormFactory formFactory, JwtHelper jwtHelper, ErrorHandler errorHandler) {
        this.formFactory = formFactory;
        this.jwtHelper = jwtHelper;
        this.errorHandler = errorHandler;
    }

    public Result signIn() {

        Form<UserSignInForm> form = formFactory.form(UserSignInForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return errorHandler.onClientError(BAD_REQUEST, "admin-signin-form-" + form.allErrors().get(0).key(),
                    form.allErrors().get(0).key() + ", " + form.allErrors().get(0).message(),
                    request().method() + " " + request().uri());
        }

        UserSignInForm userSignInForm = form.get();

        DoctorUser doctor = DoctorUser.finder.query().where().eq("username", userSignInForm.username).findOne();

        if (doctor != null && Arrays.equals(doctor.getPassword(),getEncPassword(userSignInForm.password))) {
            ObjectNode response = Json.newObject();
            response.put("token", doctor.getToken());
            response.put("type", "doctor");
            response.put("id", doctor.getId());

            return ok(response);
        }

        PatientUser patient = PatientUser.finder.query().where().eq("username", userSignInForm.username).findOne();

        if (patient != null && Arrays.equals(patient.getPassword(),(getEncPassword(userSignInForm.password)))) {
            ObjectNode response = Json.newObject();
            response.put("token", patient.getToken());
            response.put("type", "patient");
            response.put("id", patient.getId());

            return ok(response);
        }

        NurseUser nurse = NurseUser.finder.query().where().eq("username", userSignInForm.username).findOne();

        if (nurse != null && Arrays.equals(nurse.getPassword(),getEncPassword(userSignInForm.password))) {
            ObjectNode response = Json.newObject();
            response.put("token", nurse.getToken());
            response.put("type", "nurse");
            response.put("id", nurse.getId());

            return ok(response);
        }

        RelativeUser relative = RelativeUser.finder.query().where().eq("username", userSignInForm.username).findOne();

        if (relative != null && Arrays.equals(relative.getPassword(),getEncPassword(userSignInForm.password))) {
            ObjectNode response = Json.newObject();
            response.put("token", relative.getToken());
            response.put("type", "relative");
            response.put("id", relative.getId());

            return ok(response);
        }

        AdminUser admin = AdminUser.finder.query().where().eq("username", userSignInForm.username).findOne();
        byte[] password = getEncPassword(userSignInForm.password);
        if (admin != null && Arrays.equals(admin.getPassword(),password)) {

            ObjectNode response = Json.newObject();
            response.put("token", admin.getToken());
            response.put("type", "admin");
            response.put("id", admin.getId());

            return ok(response);
        }

        return notFound("Account does not found!");
    }
    private byte[] getEncPassword(String password) {
        MessageDigest digest = null;
            try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encPassword = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
            return encPassword;
    }
}
