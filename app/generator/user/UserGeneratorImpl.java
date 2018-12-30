package generator.user;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jwt.JwtHelper;
import models.DoctorUser;
import play.Logger;

@Singleton
public class UserGeneratorImpl implements UserGenerator {

    private JwtHelper jwtHelper;
    private Logger.ALogger logger = Logger.of("components.generator.user.UserGenerator");

    @Inject
    public UserGeneratorImpl(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    public DoctorUser generate(String name, String surname) throws Exception {
        logger.debug("Generating real user.");
        DoctorUser user = new DoctorUser(
                null,
                name,
                surname,
                null,
                name + surname + "@email.com");
        // since we need userId to generate token, first we should save bean.
        user.save();

        try {
            user.setToken(jwtHelper.getSignedToken(user.getId(), false));
            user.save();
        } catch (Exception e) {
            user.delete();
            throw e;
        }

        return user;
    }

}