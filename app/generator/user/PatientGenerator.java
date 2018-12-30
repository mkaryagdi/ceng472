package generator.user;

import models.DoctorUser;
import models.PatientUser;

public interface PatientGenerator {

    PatientUser generate(String username, String password, String name, String surname, Integer birthYear,
                         String address, String gender, DoctorUser doctor) throws Exception;
}
