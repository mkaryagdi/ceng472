package controllers.api;
import com.google.inject.Inject;
import handlers.ErrorHandler;
import jsonmappers.UserMapper;
import jwt.JwtHelper;
import jwt.JwtValidator;
import jwt.filter.Attrs;
import models.DoctorUser;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

public class DoctorUserController extends Controller {

    private UserMapper doctorMapper;
    private JwtValidator jwtValidator;
    private JwtHelper jwtHelper;
    private ErrorHandler errorHandler;

    @Inject
    public DoctorUserController(UserMapper doctorMapper, JwtValidator jwtValidator, JwtHelper jwtHelper, ErrorHandler errorHandler) {
        this.doctorMapper = doctorMapper;
        this.jwtValidator = jwtValidator;
        this.jwtHelper = jwtHelper;
        this.errorHandler = errorHandler;
    }

    public Result fetch(Long id) {

        Long userId = request().attrs().get(Attrs.VERIFIED_JWT).getUserId();
        DoctorUser verifiedUser = DoctorUser.finder.byId(userId);

        if(verifiedUser.getId().equals(id)) {
            return ok(doctorMapper.getAllData(verifiedUser));
        } else {
            DoctorUser user = DoctorUser.finder.byId(id);

            if (user == null) {
                return errorHandler.onClientError(NOT_FOUND, "doctoruser-fetch-notfound",
                        "There is no user by this id.",
                        request().method() + " " + request().uri());
            }

            return ok(doctorMapper.getPartialData(user));
        }
    }

}
