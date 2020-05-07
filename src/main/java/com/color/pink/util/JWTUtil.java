package com.color.pink.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author HarrisonLee
 * @date 2020/5/6 22:41
 */
public class JWTUtil {

    private static final int EXP_DAYS = 30;

    public static String createJWT(String username, String password){
        final var algorithm = Algorithm.HMAC256(password);
        final var token = JWT.create()
                .withClaim("username", username)
                .withExpiresAt(Date.from(LocalDateTime.now().plusDays(EXP_DAYS)
                        .atZone(ZoneId.of("Asia/Shanghai")).toInstant()))
                .sign(algorithm);
        return token;
    }

    public static boolean verify(String token, String username,  String password) {
        try {
            final var algorithm = Algorithm.HMAC256(password);
            final var verifier = JWT.require(algorithm).withClaim("username", username).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
