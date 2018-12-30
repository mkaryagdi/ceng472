package handlers;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import generator.user.UserGenerator;
import generator.user.UserGeneratorImpl;
import jwt.JwtHelper;
import models.DoctorUser;
import models.User;
import play.Logger;

import javax.inject.Singleton;

@Singleton
public class DatabaseHandler {

    private UserGenerator userGenerator;
    private JwtHelper jwtHelper;

    @Inject
    public DatabaseHandler(UserGeneratorImpl userGenerator, JwtHelper jwtHelper) {
        this.userGenerator = userGenerator;
        this.jwtHelper = jwtHelper;
        start();
    }

    public void start() {
        Logger.info("DB handler is started...");
        checkDoctors();
        //checkAdmin();
        Logger.info("DB handler successfully completed!");
    }

    private void checkDoctors() {

        if (DoctorUser.finder.all().isEmpty()) {
            Logger.info("Test doctors are missing. Inserting default values...");

            try {
                DoctorUser doctor1 = userGenerator.generate("Elif", "Duran");
                doctor1.save();

                DoctorUser doctor2 = userGenerator.generate("Murat", "Karyagdi");
                doctor2.save();

                DoctorUser doctor3 = userGenerator.generate("Can", "Candir");
                doctor3.save();

            } catch (Exception e) {
                Logger.error("error", e);
            }

            Logger.info("Test doctors inserted.");
        }

    }

//    private void checkAdmin() {
//
//        if(Admin.finder.all().isEmpty()) {
//            Logger.info("Admin accounts are missing. Inserting default values...");
//
//            HashMap<String, String> accountMap = (HashMap<String, String>) config.getAnyRef("adminAccounts");
//
//            accountMap.forEach((key, value) -> {
//                Admin admin = new Admin(key + "@uncosoft.com", BCrypt.hashpw(value, BCrypt.gensalt()));
//                admin.save();
//
//                try {
//                    admin.setToken(jwtHelper.getSignedToken(admin.getId(), true, false));
//                    admin.save();
//                } catch (UnsupportedEncodingException e) {
//                    Logger.error("create admin error", e);
//                }
//            });
//
//            Logger.info("Admin accounts inserted.");
//        }
//
//    }
//
}
