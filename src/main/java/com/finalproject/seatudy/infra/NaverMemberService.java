package com.finalproject.seatudy.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.seatudy.domain.LoginType;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.security.jwt.JwtTokenUtils;
import com.finalproject.seatudy.service.MemberService;
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

import static com.finalproject.seatudy.service.dto.response.MemberResDto.MemberOauthResDto;
import static com.finalproject.seatudy.service.dto.response.MemberResDto.fromEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverMemberService {
    private final MemberService memberService;
    private final JwtTokenUtils jwtTokenUtils;

    @Value("${security.oauth2.naver.grant_type}")
    private String NAVER_GRANT_TYPE;
    @Value("${security.oauth2.naver.client_id}")
    private String NAVER_CLIENT_ID;
    @Value("${security.oauth2.naver.client_secret}")
    private String NAVER_CLIENT_SECRET;
    @Value("${security.oauth2.naver.userinfo_uri}")
    private String NAVER_USER_INFO;

    public ResponseDto<?> naverLogin(String code, String state, HttpServletResponse response) throws JsonProcessingException {
        String naverACTokens = getNaverTokens(code, state);
        MemberOauthResDto naverUserDto = getNaverUserInfo(naverACTokens);
        Member naverMember = memberService.registerSocialLoginMemberIfNeed(naverUserDto, LoginType.NAVER);

        String naverAC = jwtTokenUtils.generateJwtToken(naverMember);
        memberService.tokenToHeaders(naverAC, response);

        Long point = memberService.calculateCurrentPoint(naverMember);
        log.info("Naver 로그인 완료: {}", naverMember.getEmail());
        return ResponseDto.success(fromEntity(naverMember, point));
    }

    private String getNaverTokens(String code, String state) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", NAVER_CLIENT_ID);
        body.add("client_secret", NAVER_CLIENT_SECRET);
        body.add("grant_type", NAVER_GRANT_TYPE);
        body.add("state", state);
        body.add("code", code);

        HttpEntity<MultiValueMap<String,String>> naverTokenRequest = new HttpEntity<>(body,headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST, naverTokenRequest, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("Naver에서 토큰받기 완료");
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private MemberOauthResDto getNaverUserInfo(String naverACTokens) throws JsonProcessingException {
        JsonNode jsonNode = MemberService.getUserInfo(naverACTokens, NAVER_USER_INFO).get("response");
        String email = jsonNode.get("email").asText();
        log.info("Naver 유저이메일: {}", email);
        return new MemberOauthResDto(email);
    }
}
