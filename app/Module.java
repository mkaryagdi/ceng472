import com.google.inject.AbstractModule;
import generator.user.DoctorGenerator;
import generator.user.DoctorGeneratorImpl;
import generator.user.NurseGenerator;
import generator.user.NurseGeneratorImpl;
import generator.user.PatientGenerator;
import generator.user.PatientGeneratorImpl;
import generator.user.RelativeGenerator;
import generator.user.RelativeGeneratorImpl;
import handlers.DatabaseHandler;
import jwt.JwtHelper;
import jwt.JwtHelperImpl;
import jwt.JwtValidator;
import jwt.JwtValidatorImpl;
import play.api.db.evolutions.DynamicEvolutions;
import play.db.ebean.DefaultEbeanConfig;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;


/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule {

    @Override
    public void configure() {

		// ebean dynamic evolution configuration order bug fix.
		bind(DynamicEvolutions.class).to(EbeanDynamicEvolutions.class).asEagerSingleton();
		bind(EbeanConfig.class).toProvider(DefaultEbeanConfig.EbeanConfigParser.class).asEagerSingleton();


		// jwt
		bind(JwtValidator.class).to(JwtValidatorImpl.class);
		bind(JwtHelper.class).to(JwtHelperImpl.class);
		bind(PatientGenerator.class).to(PatientGeneratorImpl.class);
		bind(DoctorGenerator.class).to(DoctorGeneratorImpl.class);
		bind(NurseGenerator.class).to(NurseGeneratorImpl.class);
		bind(RelativeGenerator.class).to(RelativeGeneratorImpl.class);
		bind(JwtHelper.class).to(JwtHelperImpl.class);
		bind(DatabaseHandler.class).asEagerSingleton();

    }

}
