package generator.user;

import models.DoctorUser;

public interface UserGenerator {

    DoctorUser generate(String name, String surname) throws Exception;
}
