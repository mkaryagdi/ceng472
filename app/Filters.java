import jwt.filter.JwtFilter;
import play.Environment;
import play.api.Configuration;
import play.api.http.EnabledFilters;
import play.http.DefaultHttpFilters;
import play.mvc.EssentialFilter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class Filters extends DefaultHttpFilters {

    private final Environment env;
    private final Configuration conf;
    private final EssentialFilter jwtFilter;
    private final EnabledFilters enabledFilters;

    @Inject
    public Filters(Environment env, Configuration conf, JwtFilter jwtFilter, EnabledFilters enabledFilters) {
        this.env = env;
        this.conf = conf;
        this.jwtFilter = jwtFilter;
        this.enabledFilters = enabledFilters;
    }

    private static List<EssentialFilter> combine(List<EssentialFilter> filters, EssentialFilter toAppend) {
        List<EssentialFilter> combinedFilters = new ArrayList<>(filters);
        combinedFilters.add(toAppend);
        return combinedFilters;
    }

    @Override
    public List<EssentialFilter> getFilters() {
        List<EssentialFilter> zeFilters = enabledFilters.asJava().getFilters();
        zeFilters.add(jwtFilter);
        return zeFilters;
    }
}