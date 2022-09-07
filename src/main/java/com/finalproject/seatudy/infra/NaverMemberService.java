package com.finalproject.seatudy.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.seatudy.service.dto.response.NaverUserDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import com.finalproject.seatudy.domain.LoginType;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.repository.MemberRepository;
import com.finalproject.seatudy.service.MemberService;
import com.finalproject.seatudy.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverMemberService {

    @Value("${security.oauth2.naver.grant_type}")
    private String NAVER_GRANT_TYPE;

    @Value("${security.oauth2.naver.client_id}")
    private String NAVER_CLIENT_ID;

    @Value("${security.oauth2.naver.client_secret}")
    private String NAVER_CLIENT_SECRET;

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;


    public ResponseDto<?> naverLogin(String code, String state, HttpServletResponse response) throws JsonProcessingException {
        String naverACTokens = getNaverTokens(code, state);

        NaverUserDto naverUserDto = getNaverUserInfo(naverACTokens);

        Member member = registerNaverUserIfNeed(naverUserDto);

        String naverAC = jwtTokenUtils.generateJwtToken(member);
        memberService.tokenToHeaders(naverAC, response);

        log.info("네이버 로그인 완료: {}", member.getEmail());
        return ResponseDto.success(
                NaverUserDto.builder()
                        .id(member.getMemberId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .birth(member.getBirthday())
                        .build()
        );
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

        HttpEntity<MultiValueMap<String,String>> naverTokenRequest =
                new HttpEntity<>(body,headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private NaverUserDto getNaverUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "Content-Type: application/json;charset=UTF-8");

        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest =
                new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                naverUserInfoRequest,
                String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody).get("response");
        long naverId = jsonNode.get("id").asLong();
        String email = jsonNode.get("email").asText();
        String nickname = jsonNode.get("nickname").asText();
        String birthday = jsonNode.get("birthday").asText();

        return NaverUserDto.builder()
                .id(naverId)
                .email(email)
                .nickname(nickname)
                .birth(birthday)
                .build();
    }

    private Member registerNaverUserIfNeed(NaverUserDto naverUserDto) {
        String email = naverUserDto.getEmail();
        String nickname = naverUserDto.getNickname();

        Member member = memberRepository.findByEmail(email).orElse(null);

        if(member == null) {
            String password = UUID.randomUUID().toString();

            member = Member.builder()
                    .email(email)
                    .nickname(nickname)
                    .password(passwordEncoder.encode(password))
                    .birthday(naverUserDto.getBirth())
                    .loginType(LoginType.NAVER)
                    .build();

            memberRepository.save(member);
        }

        return member;
    }
}
