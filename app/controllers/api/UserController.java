package controllers.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Singleton;
import controllers.forms.SignInUserForm;
import controllers.forms.SignUpUserForm;
import jwt.JwtHelper;
import models.User.User;
import models.User.UserGenerator;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Singleton
public class UserController extends Controller {

    private FormFactory formFactory;
    private UserGenerator userGenerator;
    private JwtHelper jwtHelper;


    @Inject
    public UserController(FormFactory formFactory, UserGenerator userGenerator,
                          JwtHelper jwtHelper){
        this.formFactory = formFactory;
        this.userGenerator = userGenerator;
        this.jwtHelper = jwtHelper;
    }

    public Result fetch(Long id) {
        User user = User.find.byId(id);

        if(user == null)
            return badRequest("user deleted or does not exist.");

        ObjectNode result = (ObjectNode) Json.toJson(user);

        return ok(result);
    }

    public Result delete(Long id) {
        User user = User.find.byId(id);

        if(user == null)
            return badRequest("user already deleted or does not exist.");

        user.delete();

        return ok("User with id " + id + " deleted.");
    }

    public Result fetchAll() {
        List<User> users = User.find.all();
        ArrayNode result = Json.newArray();

        for (User user: users){
            ObjectNode userNode = (ObjectNode) Json.toJson(user);
            result.add(userNode);
        }

        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result signUp() {

        Form<SignUpUserForm> form = formFactory.form(SignUpUserForm.class).bind(request().body().asJson());

        if(form.hasErrors())
            return badRequest(form.errorsAsJson());

        SignUpUserForm signUpUserForm = form.get();

        Logger.debug("user creating...");

        User isUserExist = User.find.query()
                .where()
                .eq("email", signUpUserForm.email)
                .findOne();

        if(isUserExist != null){
            Logger.debug("A user with this email exists.");
            return badRequest("A user with this email exists.");
        }

        User user;

        try {
            user = userGenerator.generate(signUpUserForm.name, signUpUserForm.email, signUpUserForm.password);
            Logger.debug("user generated with id: " + user.getId());
        } catch (UnsupportedEncodingException e){
            return badRequest("Encoding error: " + e.getMessage());
        }

        ObjectNode res = (ObjectNode) Json.toJson(user);

        return ok(res);
    }

    public Result signIn() {

        Form<SignInUserForm> form = formFactory.form(SignInUserForm.class).bind(request().body().asJson());

        if(form.hasErrors())
            return badRequest(form.errorsAsJson());

        SignInUserForm signInUserForm = form.get();

        Logger.debug("user sign in...");

        User user = User.find.query()
                .where()
                .eq("email", signInUserForm.email)
                .and()
                .eq("password", signInUserForm.password)
                .findOne();

        if(user == null)
            return badRequest("email or password is wrong!");

        try {
            user.setToken(jwtHelper.getSignedToken(user.getId(), false));
            user.save();
        } catch (UnsupportedEncodingException e) {
            return badRequest("Encoding error: " + e.getMessage());
        }


        return ok(Json.newObject().put("token", user.getToken()));
    }

}
