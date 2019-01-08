package generator.user;

import jwt.JwtHelper;
import models.Record;
import models.RelativeUser;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RelativeGeneratorImpl implements RelativeGenerator {

    private JwtHelper jwtHelper;

    @Inject
    public RelativeGeneratorImpl(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    public RelativeUser generate(String username, String password, String name, String surname,
                                 Double phoneNumber, Record record) throws Exception {

        Logger.debug("Generating relative user.");
        RelativeUser user = new RelativeUser(
                null,
                username,
                password,
                name,
                surname,
                phoneNumber);
        // since we need userId to generate token, first we should save bean.
        if (record != null)
            record.getPatientUser().addRelative(user);
//        record.save();
        user.save();

        try {
            user.setToken(jwtHelper.getSignedToken(user.getId(), false, false, false, false, true));
            user.save();
        } catch (Exception e) {
            user.delete();
            throw e;
        }

        return user;
    }
}
