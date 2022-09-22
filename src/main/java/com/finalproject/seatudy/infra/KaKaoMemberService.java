package com.finalproject.seatudy.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.seatudy.domain.LoginType;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.Rank;
import com.finalproject.seatudy.domain.repository.RankRepository;
import com.finalproject.seatudy.security.jwt.JwtTokenUtils;
import com.finalproject.seatudy.service.MemberService;
import com.finalproject.seatudy.service.dto.response.MemberResDto;
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
import java.util.List;

import static com.finalproject.seatudy.service.util.CalendarUtil.totalPoint;


@Slf4j
@Service
@RequiredArgsConstructor
public class KaKaoMemberService {
    private final MemberService memberService;
    private final JwtTokenUtils jwtTokenUtils;
    private final RankRepository rankRepository;

    @Value("${security.oauth2.kakao.client_id}")
    private String KAKAO_CLIENT_ID;
    @Value("${security.oauth2.kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URI;


    public ResponseDto<?> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        String kakaoACTokens = getKakaoTokens(code);

        MemberResDto kakaoUserInfo = getKakaoUserInfo(kakaoACTokens);

        Member kakaoMember = memberService.registerSocialLoginMemberIfNeed(kakaoUserInfo, LoginType.KAKAO);

        String kakaoAC = jwtTokenUtils.generateJwtToken(kakaoMember);
        memberService.tokenToHeaders(kakaoAC, response);

        log.info("kakao 로그인 완료: {}",kakaoMember.getEmail());
        List<Rank> allMemberList = rankRepository.findAllByMember(kakaoMember);

        Long point = totalPoint(allMemberList);

        log.info("카카오 로그인 완료: {}",kakaoMember.getEmail());
        return ResponseDto.success(
                MemberResDto.builder()
                        .id(kakaoMember.getMemberId())
                        .email(kakaoMember.getEmail())
                        .nickname(kakaoMember.getNickname())
                        .defaultFish("니모")
                        .loginType(LoginType.KAKAO)
                        .point(point)
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

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body,headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);

        String responseBody = response.getBody(); // json 형태
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("Kakao에서 토큰받기 완료");
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    public MemberResDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "Content-Type: application/json;charset=UTF-8");


        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest =
                new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET, kakaoUserInfoRequest, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        long userId = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        log.info("Kakao에서 사용자 정보획득 완료: {}", email);
        return MemberResDto.builder()
                .id(userId)
                .email(email)
                .nickname(nickname)
                .build();
    }
}
