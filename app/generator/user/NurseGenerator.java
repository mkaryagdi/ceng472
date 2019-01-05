package generator.user;

import models.DoctorUser;
import models.NurseUser;

public interface NurseGenerator {

    NurseUser generate(String username, String password, String name, String surname,
                       String gender, String major, DoctorUser doctor) throws Exception;
}
