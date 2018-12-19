package controllers.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import controllers.forms.CommentCreateForm;
import jwt.JwtHelper;
import jwt.VerifiedJwt;
import jwt.filter.Attrs;
import models.Comment.Comment;
import models.Comment.CommentGenerator;
import models.Thread.Thread;
import models.User.User;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Singleton
public class CommentController extends Controller {

    private FormFactory formFactory;
    private CommentGenerator commentGenerator;
    private JwtHelper jwtHelper;

    @Inject
    public CommentController(FormFactory formFactory, CommentGenerator commentGenerator,
                            JwtHelper jwtHelper){
        this.formFactory = formFactory;
        this.commentGenerator = commentGenerator;
        this.jwtHelper = jwtHelper;
    }


    public Result fetch(Long threadId) {

        VerifiedJwt verifiedJwt = request().attrs().get(Attrs.VERIFIED_JWT);

        User user = User.find.byId(verifiedJwt.getUserId());

        if(user == null)
            return badRequest("user does not exist.");

        List<Comment> comments = Comment.find.query()
        .where()
        .eq("threadId", threadId)
        .findList();

        ArrayNode res = Json.newArray();

        for(Comment comment : comments){
            ObjectNode commentNode = (ObjectNode) Json.toJson(comment);
            commentNode.remove("userId");
            commentNode.replace("user", createUserNode(comment));
            res.add(commentNode);
        }

        return ok(res);
    }

    public Result create(Long threadId) {

        VerifiedJwt verifiedJwt = request().attrs().get(Attrs.VERIFIED_JWT);

        User user = User.find.byId(verifiedJwt.getUserId());

        if(user == null)
            return badRequest("user does not exist.");

        Form<CommentCreateForm> form = formFactory.form(CommentCreateForm.class).bind(request().body().asJson());

        if(form.hasErrors())
            return badRequest(form.errorsAsJson());

        CommentCreateForm commentCreateForm = form.get();

        Logger.debug("comment creating...");
        Comment comment;

        try {
            comment = commentGenerator.generate(user.getId(), threadId, commentCreateForm.msg);
            Logger.debug("comment generated with id: " + comment.getId());
            Thread thread = Thread.find.byId(comment.getThreadId());

            if(thread == null)
                return badRequest("user does not exist.");

            thread.incrementSubCount();
            thread.save();
        } catch (UnsupportedEncodingException e){
            return badRequest("Encoding error: " + e.getMessage());
        }

        ObjectNode result = (ObjectNode) Json.toJson(comment);
        result.remove("userId");
        result.replace("user", createUserNode(comment));
        return ok(result);
    }

    public Result delete(Long id) {
        Comment comment = Comment.find.byId(id);

        if(comment == null)
            return badRequest("comment already deleted or does not exist.");

        comment.delete();

        return ok("Comment with id " + id + " deleted.");
    }

    public Result vote(Long id, Boolean vote) {

        VerifiedJwt verifiedJwt = request().attrs().get(Attrs.VERIFIED_JWT);

        User user = User.find.byId(verifiedJwt.getUserId());

        if(user == null)
            return badRequest("user does not exist.");

        Comment comment = Comment.find.byId(id);

        if(comment == null)
            return badRequest("comment already deleted or does not exist.");

        if(vote){
            comment.upVote();
            Logger.debug("comment with id " + id + " vote incremented.");
        }
        else{
            comment.downVote();
            Logger.debug("comment with id " + id + " vote decremented.");
        }

        comment.save();

        return ok();
    }

    private ObjectNode createUserNode(Comment comment){
        ObjectNode node = Json.newObject();
        node.put("id", comment.getUserId());
        node.put("name", User.find.byId(comment.getUserId()).getName());
        return node;
    }

}
