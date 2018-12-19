package jwt.filter;

import akka.stream.Materializer;
import jwt.JwtValidator;
import jwt.VerifiedJwt;
import play.Logger;
import play.libs.F;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;
import play.routing.HandlerDef;
import play.routing.Router;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static play.mvc.Results.forbidden;
import static play.mvc.Results.unauthorized;

public class JwtFilter extends Filter {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String ROUTE_MODIFIER_USER_FILTER_TAG = "userFilter";
    private static final String ROUTE_MODIFIER_ADMIN_FILTER_TAG = "adminFilter";
    private static final String ERR_AUTHORIZATION_HEADER = "ERR_AUTHORIZATION_HEADER";

    private JwtValidator jwtValidator;

    @Inject
    public JwtFilter(Materializer mat, JwtValidator jwtValidator) {
        super(mat);
        this.jwtValidator = jwtValidator;
    }

    @Override
    public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> nextFilter, Http.RequestHeader requestHeader) {

        if (requestHeader.attrs().containsKey(Router.Attrs.HANDLER_DEF)) {
            HandlerDef handler = requestHeader.attrs().get(Router.Attrs.HANDLER_DEF);
            List<String> modifiers = handler.getModifiers();

            if(!modifiers.contains(ROUTE_MODIFIER_USER_FILTER_TAG) && !modifiers.contains(ROUTE_MODIFIER_ADMIN_FILTER_TAG))
                return nextFilter.apply(requestHeader);

            Optional<String> authHeader =  requestHeader.getHeaders().get(HEADER_AUTHORIZATION);

            if (!authHeader.filter(ah -> ah.contains(BEARER)).isPresent()) {
                Logger.error("f=JwtFilter, error=authHeaderNotPresent");
                return CompletableFuture.completedFuture(unauthorized(ERR_AUTHORIZATION_HEADER));
            }

            String token = authHeader.map(ah -> ah.replace(BEARER, "")).orElse("");
            F.Either<jwt.JwtValidator.Error, VerifiedJwt> res = jwtValidator.verify(token);

            if (res.left.isPresent()) {
                return CompletableFuture.completedFuture(unauthorized(res.left.get().toString()));
            }

            VerifiedJwt verifiedJwt = res.right.get();

            if(modifiers.contains(ROUTE_MODIFIER_USER_FILTER_TAG) && verifiedJwt.isAdmin())
                return CompletableFuture.completedFuture(forbidden("admin is not allowed"));

            if(modifiers.contains(ROUTE_MODIFIER_ADMIN_FILTER_TAG) && !verifiedJwt.isAdmin())
                return CompletableFuture.completedFuture(forbidden("user is not allowed"));

            return nextFilter.apply(requestHeader.withAttrs(requestHeader.attrs().put(Attrs.VERIFIED_JWT, verifiedJwt)));
        }

        return nextFilter.apply(requestHeader);
    }
}