package jwt;

import java.io.UnsupportedEncodingException;

public interface JwtHelper {
    String getSignedToken(Long userId, Boolean adminGrant, Boolean doctorGrant,
                          Boolean nurseGrant, Boolean patientGrant, Boolean relativeGrant) throws UnsupportedEncodingException;
}
