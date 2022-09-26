package com.finalproject.seatudy.service;


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
import com.finalproject.seatudy.service.dto.response.MemberResDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        return ResponseDto.success(
                MemberResDto.builder()
                        .id(findMember.getMemberId())
                        .email(findMember.getEmail())
                        .nickname(findMember.getNickname())
                        .defaultFish(findMember.getDefaultFishUrl())
                        .loginType(findMember.getLoginType())
                        .point(findMember.getPoint())
                        .build()
        );
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

    public void tokenToHeaders(String token, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + token);
    }

    @Transactional
    public Member registerSocialLoginMemberIfNeed(MemberResDto memberResDto, LoginType loginType) {
        String email = memberResDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElse(null);

        int randNum = (int) (Math.random() * 10000);

        if(member == null) {
            String password = UUID.randomUUID().toString();

            member = Member.builder()
                    .email(email)
                    .nickname("Player" + randNum)
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

    public ResponseDto<?> getMyProfile(UserDetailsImpl userDetails) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        List<Rank> allMemberList = rankRepository.findAllByMember(member);
        Long point = totalPoint(allMemberList);

        MemberResDto responseDto = MemberResDto.builder()
                .id(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .defaultFish(member.getDefaultFishUrl())
                .loginType(member.getLoginType())
                .point(point)
                .build();
        return ResponseDto.success(responseDto);
    }

    public ResponseDto<?> getFishImage(String fishName) {
        Fish fishInfo = getFishByName(fishName);
        return ResponseDto.success(FishInfoResDto.builder()
                .fishNum(fishInfo.getFishId())
                .fishName(fishInfo.getFishName())
                .fishImageUrl(fishInfo.getFishImageUrl())
                .build());
    }

    @Transactional
    public ResponseDto<?> updateFishImage(UserDetailsImpl userDetails, String fishName) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        Fish foundFish = getFishByName(fishName);
        member.updateDefaultFish(foundFish.getFishImageUrl());

        return ResponseDto.success(MemberResDto.builder()
                .id(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .defaultFish(member.getDefaultFishUrl())
                .loginType(member.getLoginType())
                .point(member.getPoint())
                .build());
    }

    private Fish getFishByName(String fishName) {
        String decodeFishNameToKr = URLDecoder.decode(fishName, StandardCharsets.UTF_8);
        return fishRepository.findByFishName(decodeFishNameToKr);
    }
}
