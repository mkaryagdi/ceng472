package jwt;

import java.io.UnsupportedEncodingException;

public interface JwtHelper {
    String getSignedToken(Long userId, Boolean adminGrant) throws UnsupportedEncodingException;
}
