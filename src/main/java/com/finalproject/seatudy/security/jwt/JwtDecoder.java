package com.finalproject.seatudy.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.finalproject.seatudy.security.exception.CustomException;
import com.finalproject.seatudy.security.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;


@Component
@Slf4j
public class JwtDecoder {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public String decodeUsername(String token) throws CustomException {


        DecodedJWT decodedJWT = isValidToken(token)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_AUTH_TOKEN));

        Date expiredDate = decodedJWT
                .getExpiresAt();

        Date now = new Date();
        if (expiredDate.before(now)) {
            log.debug("access-token 기간이 만료되었습니다: {}", expiredDate);
            throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        }

        return decodedJWT
                .getSubject();
    }

    private Optional<DecodedJWT> isValidToken(String token) {
        DecodedJWT jwt = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            JWTVerifier verifier = JWT
                    .require(algorithm)
                    .build();

            jwt = verifier.verify(token);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return Optional.ofNullable(jwt);
    }
}
