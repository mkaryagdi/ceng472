package generator.user;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jwt.JwtHelper;
import models.DoctorUser;
import play.Logger;

@Singleton
public class DoctorGeneratorImpl implements DoctorGenerator {

    private JwtHelper jwtHelper;

    @Inject
    public DoctorGeneratorImpl(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    public DoctorUser generate(String username, String password, String name, String surname,
                               String major, Integer birthYear, String gender) throws Exception {

        Logger.debug("Generating doctor user.");
        DoctorUser user = new DoctorUser(
                null,
                username,
                password,
                name,
                surname,
                major,
                birthYear,
                gender);
        // since we need userId to generate token, first we should save bean.
        user.save();

        try {
            user.setToken(jwtHelper.getSignedToken(user.getId(), false, true, false, false, false));
            user.save();
        } catch (Exception e) {
            user.delete();
            throw e;
        }

        return user;
    }

}