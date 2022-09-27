package com.finalproject.seatudy.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.seatudy.domain.LoginType;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.security.jwt.JwtTokenUtils;
import com.finalproject.seatudy.service.MemberService;
import com.finalproject.seatudy.service.dto.response.MemberResDto;
import com.finalproject.seatudy.service.dto.response.MemberResDto.MemberOauthResDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@Service
@RequiredArgsConstructor
public class KaKaoMemberService {
    private final MemberService memberService;
    private final JwtTokenUtils jwtTokenUtils;

    @Value("${security.oauth2.kakao.client_id}")
    private String KAKAO_CLIENT_ID;
    @Value("${security.oauth2.kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URI;
    @Value("${security.oauth2.kakao.userinfo_uri}")
    private String KAKAO_USER_INFO;

    public ResponseDto<?> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        String kakaoACTokens = getKakaoTokens(code);
        MemberOauthResDto kakaoUserInfo = getKakaoUserInfo(kakaoACTokens);
        Member kakaoMember = memberService.registerSocialLoginMemberIfNeed(kakaoUserInfo, LoginType.KAKAO);

        String kakaoAC = jwtTokenUtils.generateJwtToken(kakaoMember);
        memberService.tokenToHeaders(kakaoAC, response);

        Long point = memberService.calculateCurrentPoint(kakaoMember);
        log.info("kakao 로그인 완료: {}",kakaoMember.getEmail());
        return ResponseDto.success(MemberResDto.fromEntity(kakaoMember, point));
    }

    public String getKakaoTokens(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", KAKAO_CLIENT_ID);
        body.add("redirect_uri", KAKAO_REDIRECT_URI);
        body.add("code",code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body,headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST, kakaoTokenRequest, String.class);

        String responseBody = response.getBody(); // json 형태
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("Kakao에서 토큰받기 완료");
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    public MemberOauthResDto getKakaoUserInfo(String kakaoACTokens) throws JsonProcessingException {
        JsonNode jsonNode = MemberService.getUserInfo(kakaoACTokens, KAKAO_USER_INFO);
        String email = jsonNode.get("kakao_account").get("email").asText();
        log.info("Kakao 유저이메일: {}", email);
        return new MemberOauthResDto(email);
    }
}
