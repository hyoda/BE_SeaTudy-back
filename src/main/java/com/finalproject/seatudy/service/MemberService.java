package com.finalproject.seatudy.service;


import com.finalproject.seatudy.domain.LoginType;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.repository.MemberRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.security.exception.CustomException;
import com.finalproject.seatudy.security.exception.ErrorCode;
import com.finalproject.seatudy.service.dto.request.NicknameReqDto;
import com.finalproject.seatudy.service.dto.response.MemberResDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public ResponseDto<?> updateNickname(UserDetailsImpl userDetails, NicknameReqDto nicknameReqDto) {
        Member findMember = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        if(nicknameDupCheck(userDetails, nicknameReqDto)){
            findMember.update(nicknameReqDto);
            log.info("{} / 닉네임 정보변경: {}", findMember.getEmail(), findMember.getNickname());
        }

        return ResponseDto.success(
                MemberResDto.builder()
                        .id(findMember.getMemberId())
                        .email(findMember.getEmail())
                        .nickname(findMember.getNickname())
                        .loginType(findMember.getLoginType())
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
        String name = memberResDto.getNickname();
        Member member = memberRepository.findByEmail(email).orElse(null);

        if(member == null) {
            String password = UUID.randomUUID().toString();

            member = Member.builder()
                    .email(email)
                    .nickname(name)
                    .password(passwordEncoder.encode(password))
                    .loginType(loginType)
                    .build();

            memberRepository.save(member);
        } else if (!member.getLoginType().equals(loginType)) {
            throw new CustomException(ErrorCode.DUPLICATE_REGISTER);
        }
        return member;
    }

    public ResponseDto<?> logout(HttpServletRequest request) {
        request.removeAttribute("Authorization");
        return ResponseDto.success("로그아웃되었습니다.");
    }
}
