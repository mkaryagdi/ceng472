import com.google.inject.AbstractModule;
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
		bind(JwtValidator.class).to(JwtValidatorImpl.class).asEagerSingleton();
		bind(JwtHelper.class).to(JwtHelperImpl.class).asEagerSingleton();

    }

}
