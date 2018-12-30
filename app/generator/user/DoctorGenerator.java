package generator.user;

import models.DoctorUser;

public interface DoctorGenerator {

    DoctorUser generate(String username, String password, String name, String surname,
                        String major, Integer birthYear, String gender) throws Exception;
}
