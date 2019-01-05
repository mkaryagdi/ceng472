package handlers;

import com.google.inject.Inject;
import generator.user.DoctorGenerator;
import generator.user.DoctorGeneratorImpl;
import generator.user.PatientGenerator;
import generator.user.PatientGeneratorImpl;
import jwt.JwtHelper;
import models.DoctorUser;
import models.PatientUser;
import play.Logger;

import javax.inject.Singleton;

@Singleton
public class DatabaseHandler {

    private DoctorGenerator doctorGenerator;
    private PatientGenerator patientGenerator;
    private JwtHelper jwtHelper;

    @Inject
    public DatabaseHandler(DoctorGeneratorImpl doctorGenerator, PatientGeneratorImpl patientGenerator, JwtHelper jwtHelper) {
        this.doctorGenerator = doctorGenerator;
        this.patientGenerator = patientGenerator;
        this.jwtHelper = jwtHelper;
        start();
    }

    public void start() {
        Logger.info("DB handler is started...");
        checkDoctorsandPatients();
        //checkAdmin();
        Logger.info("DB handler successfully completed!");
    }

    private void checkDoctorsandPatients() {

        if (DoctorUser.finder.all().isEmpty() && PatientUser.finder.all().isEmpty()) {
            Logger.info("Test doctors and patients are missing. Inserting default values...");

            try {
                DoctorUser doctor1 = doctorGenerator.generate("DoktorElif", "Duran",
                        "Elif", "Duran", "Anesthesia", 1997, "female");

                DoctorUser doctor2 = doctorGenerator.generate("DoktorMurat", "Karyagdi",
                        "Murat", "Karyagdi", "Internal Diseases", 1969, "male");

                DoctorUser doctor3 = doctorGenerator.generate("CCandir", "Candir",
                        "Can", "Candir", "Dermatology", 1990, "male");

                PatientUser patient1 = patientGenerator.generate("BBakar", "Bakar",
                        "Batuhan", "Bakar", 1996, "Edirne", "male",
                        doctor1);

                PatientUser patient2 = patientGenerator.generate("EBolukbasi", "Bolukbasi",
                        "Elif", "Bolukbasi", 1994, "Bandirma", "female",
                        doctor2);

                PatientUser patient3 = patientGenerator.generate("BGulbas", "Gulbas",
                        "Baran", "Gulbas", 2000, "Ankara", "male",
                        doctor3);

            } catch (Exception e) {
                Logger.error("error", e);
            }

            Logger.info("Test doctors and patients are inserted.");
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
