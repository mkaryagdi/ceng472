package controllers.forms;

import play.data.validation.Constraints.Required;

public class CommentCreateForm {

    @Required
    public String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
