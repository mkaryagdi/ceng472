package handlers;

import com.google.inject.Inject;
import controllers.api.AdminController;
import generator.user.*;
import jwt.JwtHelper;
import models.*;
import play.Logger;

import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;

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
        checkDoctorsandPatientsandRelativesandRecords();
        checkNurses();
        checkAdmin();
        Logger.info("DB handler successfully completed!");
    }

    private void checkDoctorsandPatientsandRelativesandRecords() {

        if (DoctorUser.finder.all().isEmpty() && PatientUser.finder.all().isEmpty()) {
            Logger.info("Test doctors and patients are missing. Inserting default values...");

            try {
                DoctorUser doctor1 = doctorGenerator.generate("DoktorElif", "Elif",
                        "Elif", "Duran", "Anesthesia", 1997, "female");

                DoctorUser doctor2 = doctorGenerator.generate("DoktorMurat", "Murat",
                        "Murat", "Karyagdi", "Internal Diseases",1969, "male");

                DoctorUser doctor3 = doctorGenerator.generate("CCandir","Candir",
                        "Can", "Candir", "Dermatology", 1990, "male");


                PatientUser patient1 = patientGenerator.generate("BBakar","Bakar",
                        "Batuhan", "Bakar", 1996, "Edirne", "male",
                        doctor1);

                PatientUser patient2 = patientGenerator.generate("EBolukbasi", "Bolukbasi",
                        "Elif", "Bolukbasi", 1994, "Bandirma", "female",
                        doctor2);

                PatientUser patient3 = patientGenerator.generate("BGulbas","Gulbas",
                        "Baran", "Gulbas", 2000, "Ankara", "male",
                        doctor3);

                Record record1 = new Record("diagnostic", patient1, doctor1);
                Record record2 = new Record("diagnostic", patient2, doctor2);
                Record record3 = new Record("diagnostic", patient3, doctor3);

                RelativeUser relative1 = relativeGenerator.generate("r1",  "r1",
                        "Arda", "Ermis", 45356.0, record1);

                RelativeUser relative2 = relativeGenerator.generate("r2",  "r2",
                        "Caglar", "Yilmaz", 34567.9, record2);

                RelativeUser relative3 = relativeGenerator.generate("r3",  "r3",
                        "Damla", "Karadag", 45678.0, record3);

                relative1.save();
                relative2.save();
                relative3.save();

                record1.save();
                record2.save();
                record3.save();

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

    private void checkAdmin() {

        if(AdminUser.finder.all().isEmpty()) {
            Logger.info("Admin accounts are missing. Inserting default values...");

           AdminUser admin = new AdminUser(null, "admin", "admin");
           admin.save();

            try {
                admin.setToken(jwtHelper.getSignedToken(admin.getId(), true, false, false, false, false));
                admin.save();
            } catch (UnsupportedEncodingException e) {
                Logger.error("create admin error", e);
            }

            Logger.info("Admin account is inserted.");
        }

    }

}
