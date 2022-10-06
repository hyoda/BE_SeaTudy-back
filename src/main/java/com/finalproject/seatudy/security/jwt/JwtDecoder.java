package com.finalproject.seatudy.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.repository.MemberRepository;
import com.finalproject.seatudy.security.exception.CustomException;
import com.finalproject.seatudy.security.exception.ErrorCode;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtDecoder {

    @Value("${jwt.secret}")
    private String JWT_SECRET;
    private final MemberRepository memberRepository;

    public String decodeUsername(String token){
        DecodedJWT decodedJWT = isValidToken(token).orElseThrow(
                () -> new AuthenticationServiceException("토큰이 유효하지 않습니다"));
        Date expiredDate = decodedJWT.getExpiresAt();

        Date now = new Date();
        if(expiredDate.before(now)){
            log.error("토큰 유효시간이 만료되었습니다. (member: {})", decodedJWT.getSubject());
            throw new AuthenticationServiceException("토큰 유효시간이 만료되었습니다.");
        }
        return decodedJWT.getSubject();
    }

    public Member getMember(String token) {
        String accessToken = token.substring(7);
        String email = decodeUsername(accessToken);

        return memberRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }

    public Optional<DecodedJWT> isValidToken(String token){

        DecodedJWT jwt = null;
        try{
            Algorithm algorithm = Algorithm.HMAC256(Decoders.BASE64.decode(JWT_SECRET));
            JWTVerifier verifier = JWT
                                    .require(algorithm)
                                    .build();

            jwt = verifier.verify(token);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return Optional.ofNullable(jwt);
    }
}
