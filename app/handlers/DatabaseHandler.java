package handlers;

import com.google.inject.Inject;
import generator.user.*;
import jwt.JwtHelper;
import models.DoctorUser;
import models.NurseUser;
import models.PatientUser;
import models.Record;
import models.RelativeUser;
import play.Logger;

import javax.inject.Singleton;

@Singleton
public class DatabaseHandler {

    private DoctorGenerator doctorGenerator;
    private PatientGenerator patientGenerator;
    private NurseGenerator nurseGenerator;
    private RelativeGeneratorImpl relativeGenerator;
    private JwtHelper jwtHelper;

    @Inject
    public DatabaseHandler(DoctorGeneratorImpl doctorGenerator, PatientGeneratorImpl patientGenerator,
                           NurseGeneratorImpl nurseGenerator, RelativeGeneratorImpl relativeGenerator,
                           JwtHelper jwtHelper) {
        this.doctorGenerator = doctorGenerator;
        this.patientGenerator = patientGenerator;
        this.nurseGenerator = nurseGenerator;
        this.relativeGenerator = relativeGenerator;
        this.relativeGenerator = relativeGenerator;
        this.jwtHelper = jwtHelper;
        start();
    }

    public void start() {
        Logger.info("DB handler is started...");
        checkDoctorsandPatients();
        checkNurses();
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

                Record record1 = new Record("diagnostic", patient1, doctor1);
                Record record2 = new Record("diagnostic", patient2, doctor2);
                Record record3 = new Record("diagnostic", patient3, doctor3);

                RelativeUser relative1 = relativeGenerator.generate("relative1", "relative1",
                        "relative1", "relative1", 05330009876.0, patient1, doctor1);
                patient1.addRelative(relative1);

                RelativeUser relative2 = relativeGenerator.generate("relative2", "relative2",
                        "relative2", "relative2", 05330009876.0, patient2, doctor2);
                patient2.addRelative(relative2);

                RelativeUser relative3 = relativeGenerator.generate("relative3", "relative3",
                        "relative3", "relative3", 05330009876.0, patient3, doctor3);
                patient3.addRelative(relative3);
                relative1.addRecords(record1);
                relative1.addRecords(record2);
                relative1.addRecords(record3);

                doctor1.save();
                doctor2.save();
                doctor3.save();


            } catch (Exception e) {
                Logger.error("error", e);
            }

            Logger.info("Test doctors and patients are inserted.");
        }
    }

    private void checkNurses() {

        if (NurseUser.finder.all().isEmpty()) {
            Logger.info("Test nurses are missing. Inserting default values...");

            try {
                NurseUser nurse1 = nurseGenerator.generate("NurseSems", "Sems",
                        "Sems", "Ozdemirden", "male", "kids", null);

                NurseUser nurse2 = nurseGenerator.generate("NurseSeray", "Donmez",
                        "Seray", "Donmez", "female", "teeth", null);

                NurseUser nurse3 = nurseGenerator.generate("NurseDogus", "Eraytac",
                        "Dogus", "Eraytac", "male", "chemistry", null);

                nurse1.save();
                nurse2.save();
                nurse3.save();

            } catch (Exception e) {
                Logger.error("error", e);
            }

            Logger.info("Test nurses are inserted.");
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
