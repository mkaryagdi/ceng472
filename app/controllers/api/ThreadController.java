package controllers.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import controllers.forms.ThreadCreateForm;
import jwt.JwtHelper;
import jwt.VerifiedJwt;
import jwt.filter.Attrs;
import models.Thread.Thread;
import models.Thread.ThreadGenerator;
import models.User.User;
import org.h2.store.fs.FileUtils;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Singleton
public class ThreadController extends Controller {

    private FormFactory formFactory;
    private ThreadGenerator threadGenerator;
    private JwtHelper jwtHelper;

    @Inject
    public ThreadController(FormFactory formFactory, ThreadGenerator threadGenerator,
                          JwtHelper jwtHelper){
        this.formFactory = formFactory;
        this.threadGenerator = threadGenerator;
        this.jwtHelper = jwtHelper;
    }

    public Result fetchAll() {

        VerifiedJwt verifiedJwt = request().attrs().get(Attrs.VERIFIED_JWT);

        User user = User.find.byId(verifiedJwt.getUserId());

        if(user == null)
            return badRequest("user does not exist.");

        List<Thread> threads = Thread.find.all();
        // TODO : add order by vote
        ArrayNode result = Json.newArray();

        for (Thread thread: threads){
            ObjectNode threadNode = (ObjectNode) Json.toJson(thread);
            threadNode.remove("userId");
            threadNode.replace("user", createUserNode(thread));
            result.add(threadNode);
        }

        return ok(result);
    }

    public Result fetch(Long id) {

        VerifiedJwt verifiedJwt = request().attrs().get(Attrs.VERIFIED_JWT);

        User user = User.find.byId(verifiedJwt.getUserId());

        if(user == null)
            return badRequest("user does not exist.");

        Thread thread = Thread.find.byId(id);

        if(thread == null)
            return badRequest("thread deleted or does not exist.");

        ObjectNode result = (ObjectNode) Json.toJson(thread);
        result.remove("userId");
        result.replace("user", createUserNode(thread));

        return ok(result);
    }

    public Result delete(Long id) {

        // TODO : only I can delete my own threads
        // TODO : delete all the comments of thread too

        VerifiedJwt verifiedJwt = request().attrs().get(Attrs.VERIFIED_JWT);

        User user = User.find.byId(verifiedJwt.getUserId());

        if(user == null)
            return badRequest("user does not exist.");

        Thread thread = Thread.find.byId(id);

        if(thread == null)
            return badRequest("thread already deleted or does not exist.");

        thread.delete();

        return ok("Thread with id " + id + " deleted.");
    }

    @BodyParser.Of(BodyParser.MultipartFormData.class)
    public Result create() throws IOException {

        VerifiedJwt verifiedJwt = request().attrs().get(Attrs.VERIFIED_JWT);

        User user = User.find.byId(verifiedJwt.getUserId());

        if(user == null)
            return badRequest("user does not exist.");

        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("image");
        String title = body.asFormUrlEncoded().get("title")[0];
        String msg = body.asFormUrlEncoded().get("msg")[0];

        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();

            Files.copy(file, new File(fileName));

            Logger.debug(fileName + contentType +  file);

            Logger.debug("thread creating...");
            Thread thread;

            thread = threadGenerator.generate(user.getId(), "http://35.205.115.88/" + fileName, title, msg);
            Logger.debug("thread generated with id: " + user.getId());

            ObjectNode result = (ObjectNode) Json.toJson(thread);
            result.remove("userId");
            result.replace("user", createUserNode(thread));
            return ok(result);

        } else {
            flash("error", "Missing file");
            return badRequest();
        }

    }

    public Result vote(Long id, Boolean vote) {

        VerifiedJwt verifiedJwt = request().attrs().get(Attrs.VERIFIED_JWT);

        User user = User.find.byId(verifiedJwt.getUserId());

        if(user == null)
            return badRequest("user does not exist.");

        Thread thread = Thread.find.byId(id);

        if(thread == null)
            return badRequest("thread already deleted or does not exist.");

        if(vote){
            thread.upVote();
            Logger.debug("Thread with id " + id + " vote incremented.");
        }
        else{
            thread.downVote();
            Logger.debug("Thread with id " + id + " vote decremented.");
        }

        thread.save();

        return ok();
    }

    private ObjectNode createUserNode(Thread thread){
        ObjectNode node = Json.newObject();
        node.put("id", thread.getUserId());
        node.put("name", User.find.byId(thread.getUserId()).getName());
        return node;
    }

}
