package jsonmappers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.DoctorUser;
import play.libs.Json;

public class UserMapper {

    public ObjectNode getPartialData(DoctorUser user) {
        ObjectNode result = Json.newObject();
        result.put("name", user.getName());
        result.put("surname", user.getSurname());
        result.put("major", user.getMajor());
        result.put("email", user.getEmail());
        return result;
    }

    public ObjectNode getAllData(DoctorUser user) {
        ObjectNode result = (ObjectNode) Json.toJson(user);
        return result;
    }

}