package com.color.pink;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author HarrisonLee
 * @date 2020/5/6 20:30
 */
public class JWTTest {

    private String username = "HarrisonLee";

    private String password = "ZLee520~~~";

    private static final long EXPIRE_DAYS = 30;

    @Test
    public void createJWT(){
        final var algorithm = Algorithm.HMAC256(password);
        final var token = JWT.create()
                .withClaim("username", this.username)
                .withExpiresAt(Date.from(LocalDateTime.now().plusSeconds(10)
                        .atZone(ZoneId.of("Asia/Shanghai")).toInstant()))
                .sign(algorithm);
        System.out.println(token);
    }

    @Test
    public void verify() {
        try {
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODg3NzAwOTEsInVzZXJuYW1lIjoiSGFycmlzb" +
                    "25MZWUifQ.ytSdcVoFiIkngVMa0eskpOEUEiMy5ZGNl3brBZT6iVc";

            final var decode = JWT.decode(token);
            final var username = decode.getClaim("username").asString();
            System.out.println(username);

            final var algorithm = Algorithm.HMAC256("ZLee520~~");
            final var verifier = JWT.require(algorithm).withClaim("username", "HarrisonLee").build();
            final var jwt = verifier.verify(token);
            System.out.println("验证通过");
        } catch (InvalidClaimException e) {
            e.printStackTrace();
            System.out.println("用户名不正确");
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            System.out.println("密码不正确或token已过期");
        }
    }
}
