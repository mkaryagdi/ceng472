package generator.user;

import com.google.inject.Inject;
import jwt.JwtHelper;
import models.DoctorUser;
import models.PatientUser;
import play.Logger;

import javax.inject.Singleton;

@Singleton
public class PatientGeneratorImpl implements PatientGenerator {

    private JwtHelper jwtHelper;

    @Inject
    public PatientGeneratorImpl(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    public PatientUser generate(String username, String password, String name, String surname, Integer birthYear,
                                String address, String gender, DoctorUser doctor) throws Exception {
        Logger.debug("Generating patient user.");
        PatientUser user = new PatientUser(
                null,
                username,
                password,
                name,
                surname,
                birthYear,
                address,
                gender,
                doctor);
        // since we need userId to generate token, first we should save bean.
        user.save();

        try {
            user.setToken(jwtHelper.getSignedToken(user.getId(), false, false, false, true, false));
            user.save();
        } catch (Exception e) {
            user.delete();
            throw e;
        }

        return user;
    }
}
