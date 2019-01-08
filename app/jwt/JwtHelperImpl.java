package jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class JwtHelperImpl implements JwtHelper {

    public String getSignedToken(Long userId, Boolean adminGrant, Boolean doctorGrant,
                                 Boolean nurseGrant, Boolean patientGrant, Boolean relativeGrant) throws UnsupportedEncodingException {
        String secret = "secret";

        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("hospital")
                .withClaim("user_id", userId)
                .withClaim("admin_grant", adminGrant)
                .withClaim("doctor_grant", doctorGrant)
                .withClaim("nurse_grant", nurseGrant)
                .withClaim("patient_grant", patientGrant)
                .withClaim("relative_grant", relativeGrant)
                .withExpiresAt(Date.from(ZonedDateTime.now(ZoneId.systemDefault()).plusDays(30L).toInstant()))
                .sign(algorithm);
    }

}
