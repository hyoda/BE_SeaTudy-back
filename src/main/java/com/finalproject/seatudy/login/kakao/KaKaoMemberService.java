package com.finalproject.seatudy.login.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.seatudy.dto.response.KakaoUserDto;
import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.login.LoginType;
import com.finalproject.seatudy.login.Member;
import com.finalproject.seatudy.login.MemberRepository;
import com.finalproject.seatudy.login.MemberService;
import com.finalproject.seatudy.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KaKaoMemberService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtTokenUtils jwtTokenUtils;

    @Value("${security.oauth2.kakao.client_id}")
    private String KAKAO_CLIENT_ID;

    @Value("${security.oauth2.kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URI;



    public ResponseDto<?> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        String kakaoACTokens = getKakaoTokens(code);

        KakaoUserDto kakaoUserInfo = getKakaoUserInfo(kakaoACTokens);

        Member kakaoMember = registerKakaoUserIfNeed(kakaoUserInfo);

        String kakaoAC = jwtTokenUtils.generateJwtToken(kakaoMember);
        memberService.tokenToHeaders(kakaoAC, response);

        log.info("카카오 로그인 완료: {}",kakaoMember.getEmail());
        return ResponseDto.success(
                KakaoUserDto.builder()
                        .id(kakaoMember.getMemberId())
                        .email(kakaoMember.getEmail())
                        .nickname(kakaoMember.getNickname())
                        .build());
    }


    public String getKakaoTokens(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", KAKAO_CLIENT_ID);
        body.add("redirect_uri", KAKAO_REDIRECT_URI);
        body.add("code",code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body,headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);

        String responseBody = response.getBody(); // json 형태
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    public KakaoUserDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "Content-Type: application/json;charset=UTF-8");


        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest =
                new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET, kakaoUserInfoRequest, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        long userId = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        return new KakaoUserDto(userId, email, nickname);
    }


    private Member registerKakaoUserIfNeed(KakaoUserDto kakaoUserDto) {
        String email = kakaoUserDto.getEmail();
        String nickname = kakaoUserDto.getNickname();
        Member kakaoMember = memberRepository.findByEmail(email).orElse(null);

        if(kakaoMember == null) {
            String password = UUID.randomUUID().toString();

            kakaoMember = Member.builder()
                    .email(email)
                    .nickname(nickname)
                    .password(passwordEncoder.encode(password))
                    .loginType(LoginType.KAKAO)
                    .build();

            memberRepository.save(kakaoMember);
        }
        return kakaoMember;
    }
    
}
