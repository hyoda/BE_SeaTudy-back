package com.finalproject.seatudy.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.finalproject.seatudy.login.Member;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public final class JwtTokenUtils {
    @Value("${jwt.expiration.access-token}")
    private Long JWT_TOKEN_VALID_MILLI_SEC;
    @Value("${jwt.secret}")
    public String JWT_SECRET;

    @Value("${jwt.issuer}")
    public String CLAIM_JWT_ISSUER;


    public String generateJwtToken(Member member){
        String token = null;

        try{
            token= JWT.create()
                    .withSubject(member.getEmail())
                    .withIssuer(CLAIM_JWT_ISSUER)
                    .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALID_MILLI_SEC))
                    .sign(generateAlgorithm());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return token;
    }

//    public static String generateJwtRefreshToken(){
//        String refreshToken = null;
//
//        try{
//            refreshToken=JWT.create()
//                    .withClaim(CLAIM_EXPIRED_DATE,new Date(System.currentTimeMillis()+ JWT_REFRESH_TOKEN_VALID_MILLI_SEC))
//                    .sign(generateAlgorithm());
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        return refreshToken;
//    }

    private Algorithm generateAlgorithm(){
        return Algorithm.HMAC256(Decoders.BASE64.decode(JWT_SECRET));
    }
}
