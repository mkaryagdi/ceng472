package generator.user;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jwt.JwtHelper;
import models.DoctorUser;
import play.Logger;

@Singleton
public class UserGeneratorImpl implements UserGenerator {

    private JwtHelper jwtHelper;

    @Inject
    public UserGeneratorImpl(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    public DoctorUser generate(String email, String password) throws Exception {
        Logger.debug("Generating real user.");
        DoctorUser user = new DoctorUser(
                null,
                null,
                null,
                null,
                email,
                password);
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