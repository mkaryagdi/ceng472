package generator.user;

import jwt.JwtHelper;
import models.DoctorUser;
import models.NurseUser;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NurseGeneratorImpl implements NurseGenerator {

    private JwtHelper jwtHelper;

    @Inject
    public NurseGeneratorImpl(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    public NurseUser generate(String username, String password, String name, String surname,
                                String gender, String major, DoctorUser doctor) throws Exception {

        Logger.debug("Generating nurse user.");
        NurseUser user = new NurseUser(
                null,
                username,
                password,
                name,
                surname,
                gender,
                major,
                doctor);
        // since we need userId to generate token, first we should save bean.
        user.save();

        try {
            user.setToken(jwtHelper.getSignedToken(user.getId(), false, false, true, false, false));
            user.save();
        } catch (Exception e) {
            user.delete();
            throw e;
        }

        return user;
    }
}
