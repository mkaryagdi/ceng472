package generator.user;

import jwt.JwtHelper;
import models.DoctorUser;
import models.NurseUser;
import models.PatientUser;
import models.RelativeUser;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RelativeGeneratorImpl {

    private JwtHelper jwtHelper;

    @Inject
    public RelativeGeneratorImpl(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    public RelativeUser generate(String username, String password, String name, String surname
            , Double phoneNumber, PatientUser patient, DoctorUser doctor) throws Exception {
        Logger.debug("Generating nurse user.");
        RelativeUser user = new RelativeUser(
                null,
                username,
                password,
                name,
                surname,
                phoneNumber);
        // since we need userId to generate token, first we should save bean.
        patient.addRelative(user);
        doctor.save();
        try {
            user.setToken(jwtHelper.getSignedToken(user.getId(), false, false, false, false, true));
            doctor.save();
        } catch (Exception e) {
            user.delete();
            throw e;
        }

        return user;
    }
}
