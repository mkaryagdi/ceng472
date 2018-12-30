package jwt.filter;

import akka.stream.Materializer;
import jwt.JwtValidator;
import jwt.VerifiedJwt;
import models.*;
import play.libs.F;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;
import play.routing.Router;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static play.mvc.Results.*;

public class JwtFilter extends Filter {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String DOCTOR_USER_FILTER_TAG = "doctor";
    private static final String NURSE_USER_FILTER_TAG = "nurse";
    private static final String PATIENT_USER_FILTER_TAG = "patient";
    private static final String RELATIVE_USER_FILTER_TAG = "relative";
    private static final String ADMIN_FILTER_TAG = "adminFilter";
    private static final String UNAUTHORIZED = "UNAUTHORIZED";
    private static final String ERR_TOKEN_MISMATCH = "ERR_TOKEN_MISMATCH";
    private static final String ERR_DOCTOR_NOT_FOUND = "ERR_DOCTOR_NOT_FOUND";
    private static final String ERR_NURSE_NOT_FOUND = "ERR_NURSE_NOT_FOUND";
    private static final String ERR_PATIENT_NOT_FOUND = "ERR_PATIENT_NOT_FOUND";
    private static final String ERR_RELATIVE_NOT_FOUND = "ERR_RELATIVE_NOT_FOUND";
    private static final String ERR_ADMIN_NOT_FOUND = "ERR_ADMIN_NOT_FOUND";
    private static final String ERR_NOT_PERMITTED = "ERR_NOT_PERMITTED";
    private static final String ERR_ROUTE_NOT_FOUND = "ERR_ROUTE_NOT_FOUND";
    private static final String ERR_AUTHORIZATION_HEADER = "ERR_AUTHORIZATION_HEADER";

    private JwtValidator jwtValidator;

    @Inject
    public JwtFilter(Materializer mat, JwtValidator jwtValidator) {
        super(mat);
        this.jwtValidator = jwtValidator;
    }

    @Override
    public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> nextFilter, Http.RequestHeader requestHeader) {

        if (!requestHeader.attrs().containsKey(Router.Attrs.HANDLER_DEF))
            return CompletableFuture.completedFuture(notFound(ERR_ROUTE_NOT_FOUND));

        List<String> tags = requestHeader.attrs().get(Router.Attrs.HANDLER_DEF).getModifiers();

        Optional<String> authHeader = requestHeader.getHeaders().get(HEADER_AUTHORIZATION);

        // user or admin
        if (authHeader.filter(ah -> ah.contains(BEARER)).isPresent()) {

            String token = authHeader.map(ah -> ah.replace(BEARER, "")).orElse("");
            F.Either<jwt.JwtValidator.Error, VerifiedJwt> res = jwtValidator.verify(token);

            if (res.left.isPresent()) {
                return CompletableFuture.completedFuture(unauthorized(UNAUTHORIZED));
            }

            VerifiedJwt verifiedJwt = res.right.get();

            if (verifiedJwt.isDoctor() && tags.contains(DOCTOR_USER_FILTER_TAG)) { // doctor
                DoctorUser doctor = DoctorUser.finder.byId(verifiedJwt.getUserId());

                if (doctor == null)
                    return CompletableFuture.completedFuture(badRequest(ERR_DOCTOR_NOT_FOUND));

                if (!doctor.getToken().equals(token))
                    return CompletableFuture.completedFuture(badRequest(ERR_TOKEN_MISMATCH));

                return nextFilter.apply(requestHeader.withAttrs(requestHeader.attrs()
                        .put(Attrs.ROLE, Role.DOCTOR_USER)
                        .put(Attrs.VERIFIED_DOCTOR_USER, doctor)));
            } else if (verifiedJwt.isNurse() && (tags.contains(NURSE_USER_FILTER_TAG))) { // nurse
                NurseUser nurse = NurseUser.finder.byId(verifiedJwt.getUserId());

                if (nurse == null)
                    return CompletableFuture.completedFuture(badRequest(ERR_NURSE_NOT_FOUND));

                if (!nurse.getToken().equals(token))
                    return CompletableFuture.completedFuture(badRequest(ERR_TOKEN_MISMATCH));

                return nextFilter.apply(requestHeader.withAttrs(requestHeader.attrs()
                        .put(Attrs.ROLE, Role.NURSE_USER)
                        .put(Attrs.VERIFIED_NURSE_USER, nurse)));
            } else if (verifiedJwt.isPatient() && (tags.contains(PATIENT_USER_FILTER_TAG))) { // patient
                PatientUser patient = PatientUser.finder.byId(verifiedJwt.getUserId());

                if (patient == null)
                    return CompletableFuture.completedFuture(badRequest(ERR_PATIENT_NOT_FOUND));

                if (!patient.getToken().equals(token))
                    return CompletableFuture.completedFuture(badRequest(ERR_TOKEN_MISMATCH));

                return nextFilter.apply(requestHeader.withAttrs(requestHeader.attrs()
                        .put(Attrs.ROLE, Role.PATIENT_USER)
                        .put(Attrs.VERIFIED_PATIENT_USER, patient)));
            } else if (verifiedJwt.isRelative() && (tags.contains(RELATIVE_USER_FILTER_TAG))) { // relative
                RelativeUser relative = RelativeUser.finder.byId(verifiedJwt.getUserId());

                if (relative == null)
                    return CompletableFuture.completedFuture(badRequest(ERR_RELATIVE_NOT_FOUND));

                if (!relative.getToken().equals(token))
                    return CompletableFuture.completedFuture(badRequest(ERR_TOKEN_MISMATCH));

                return nextFilter.apply(requestHeader.withAttrs(requestHeader.attrs()
                        .put(Attrs.ROLE, Role.RELATIVE_USER)
                        .put(Attrs.VERIFIED_RELATIVE_USER, relative)));
            } else if (verifiedJwt.isAdmin() && (tags.contains(ADMIN_FILTER_TAG))) { // admin
                AdminUser admin = AdminUser.finder.byId(verifiedJwt.getUserId());

                if (admin == null)
                    return CompletableFuture.completedFuture(badRequest(ERR_ADMIN_NOT_FOUND));

                if (!admin.getToken().equals(token))
                    return CompletableFuture.completedFuture(badRequest(ERR_TOKEN_MISMATCH));

                return nextFilter.apply(requestHeader.withAttrs(requestHeader.attrs()
                        .put(Attrs.ROLE, Role.ADMIN_USER)
                        .put(Attrs.VERIFIED_ADMIN_USER, admin)));
            }

            return CompletableFuture.completedFuture(unauthorized(ERR_NOT_PERMITTED));
        }

        return CompletableFuture.completedFuture(unauthorized(ERR_AUTHORIZATION_HEADER));
    }
}