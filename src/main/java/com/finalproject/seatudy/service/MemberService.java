package com.finalproject.seatudy.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.seatudy.domain.LoginType;
import com.finalproject.seatudy.domain.entity.Fish;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.Rank;
import com.finalproject.seatudy.domain.repository.FishRepository;
import com.finalproject.seatudy.domain.repository.MemberRepository;
import com.finalproject.seatudy.domain.repository.RankRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.security.exception.CustomException;
import com.finalproject.seatudy.security.exception.ErrorCode;
import com.finalproject.seatudy.service.dto.request.NicknameReqDto;
import com.finalproject.seatudy.service.dto.response.FishInfoResDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.finalproject.seatudy.service.dto.response.MemberResDto.MemberOauthResDto;
import static com.finalproject.seatudy.service.dto.response.MemberResDto.fromEntity;
import static com.finalproject.seatudy.service.util.CalendarUtil.totalPoint;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RankRepository rankRepository;
    private final FishRepository fishRepository;

    @Transactional
    public ResponseDto<?> updateNickname(UserDetailsImpl userDetails, NicknameReqDto nicknameReqDto) {
        Member findMember = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        if(nicknameDupCheck(userDetails, nicknameReqDto)){
            findMember.updateNickname(nicknameReqDto);
            log.info("{} / 닉네임 정보변경: {}", findMember.getEmail(), findMember.getNickname());
        }
        return ResponseDto.success(fromEntity(findMember, findMember.getPoint()));
    }

    @Transactional
    public boolean nicknameDupCheck(UserDetailsImpl userDetails, NicknameReqDto nicknameReqDto) {
        if(nicknameReqDto.getNickname() == null || nicknameReqDto.getNickname().equals(""))
            throw new CustomException(ErrorCode.EMPTY_NICKNAME);

        Member member = memberRepository.findByNickname(userDetails.getMember().getNickname()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        Optional<Member> checkMember = memberRepository.findByNickname(nicknameReqDto.getNickname());

        if (checkMember.isPresent()) {
            if (member.getNickname().equals(nicknameReqDto.getNickname()))
                throw new CustomException(ErrorCode.CURRENT_NICKNAME);
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return true;
    }

    // Oauth 로그인시 카카오,네이버,구글로 부터 회원정보 받아오기 위한 메서드
    public static JsonNode getUserInfo(String accessTokens, String userInfoUri) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessTokens);
        headers.add("Content-type", "Content-Type: application/json;charset=UTF-8");

        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(userInfoUri, HttpMethod.GET, userInfoRequest, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("유저정보 획득을 위한 통신완료");
        return objectMapper.readTree(responseBody);
    }

    /*
    SeaTUDY의 회원이 아닐 경우, 강제 회원가입을 위한 메서드
    최초 가입시, 회원 닉네임은 임의로 지정
     */
    @Transactional
    public Member registerSocialLoginMemberIfNeed(MemberOauthResDto memberOauthResDto, LoginType loginType) {
        String email = memberOauthResDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElse(null);

        if(member == null) {
            String password = UUID.randomUUID().toString();
            String randNickname = "Player" + (int) (Math.random() * 10000);

            while(memberRepository.findByNickname(randNickname).isPresent()) {
                randNickname = "Player" + (int) (Math.random() * 10000);
            }

            member = Member.builder()
                    .email(email)
                    .nickname(randNickname)
                    .password(passwordEncoder.encode(password))
                    .loginType(loginType)
                    .defaultFishUrl(fishRepository.findByFishId(1L).getFishImageUrl())
                    .point(0L)
                    .build();

            memberRepository.save(member);
        } else if (!member.getLoginType().equals(loginType)) {
            throw new CustomException(ErrorCode.DUPLICATE_REGISTER);
        }
        return member;
    }

    public void tokenToHeaders(String token, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + token);
    }

    public ResponseDto<?> getMyProfile(UserDetailsImpl userDetails) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Long point = calculateCurrentPoint(member);
        return ResponseDto.success(fromEntity(member, point));
    }


    // 프로필사진(물고기 캐릭터) 수정메서드
    @Transactional
    public ResponseDto<?> updateFishImage(UserDetailsImpl userDetails, String fishName) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        Fish foundFish = getFishByName(fishName);
        member.updateDefaultFish(foundFish.getFishImageUrl());

        return ResponseDto.success(fromEntity(member, member.getPoint()));
    }

    public ResponseDto<?> getFishImage(String fishName) {
        Fish fishInfo = getFishByName(fishName);
        return ResponseDto.success(FishInfoResDto.builder()
                .fishNum(fishInfo.getFishId())
                .fishName(fishInfo.getFishName())
                .fishImageUrl(fishInfo.getFishImageUrl())
                .build());
    }

    private Fish getFishByName(String fishName) {
        String decodeFishNameToKr = URLDecoder.decode(fishName, StandardCharsets.UTF_8);
        return fishRepository.findByFishName(decodeFishNameToKr);
    }

    public Long calculateCurrentPoint(Member member) {
        List<Rank> allMemberList = rankRepository.findAllByMember(member);
        return totalPoint(allMemberList);
    }
}
