package generator.user;

import models.DoctorUser;

public interface UserGenerator {

    DoctorUser generate(String email, String password) throws Exception;
}
