package generator.user;

import models.DoctorUser;

public interface UserGenerator {

    DoctorUser generate(String username, String password) throws Exception;
}
