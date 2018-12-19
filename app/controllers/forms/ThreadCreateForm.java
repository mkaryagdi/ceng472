package controllers.forms;

import play.data.validation.Constraints.Required;

public class ThreadCreateForm {

    @Required
    public String title;

    @Required
    public String msg;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
