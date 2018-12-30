package controllers.api;

import com.google.inject.Inject;
import controllers.forms.UserSignInForm;
import handlers.ErrorHandler;
import jwt.JwtHelper;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.UnsupportedEncodingException;

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

        User user = User.finder.query().where().eq("email", userSignInForm.email).findOne();

//        if (user == null || !BCrypt.checkpw(userSignInForm.password, user.getPassword()))
//            return errorHandler.onClientError(NOT_FOUND, "admin-signin-emailpassword", "Email or password is wrong!",
//                    request().method() + " " + request().uri());

        try {
            user.setToken(jwtHelper.getSignedToken(user.getId(), false, true, false, false, false));
            user.save();
        } catch (UnsupportedEncodingException e) {
            return errorHandler.onServerError("admin-signin", e, request().method() + " " + request().uri());
        }

        return ok(Json.newObject().put("token", user.getToken()));
    }

}
