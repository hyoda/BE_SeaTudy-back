package com.finalproject.seatudy.login;


import com.finalproject.seatudy.dto.request.LoginRequestDto;
import com.finalproject.seatudy.dto.request.MemberRequestDto;
import com.finalproject.seatudy.dto.response.KakaoUserDto;
import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getUsername())) {
            return ResponseDto.fail("DUPLICATED_NICKNAME",  "중복된 이메일 입니다.");
        }

        Member member = Member.builder()
                .email(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();

        memberRepository.save(member);


        return ResponseDto.success(
                KakaoUserDto.builder()
                        .id(member.getMemberId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Member member = isPresentMember(loginRequestDto.getEmail());
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        if (!member.validatePassword(passwordEncoder, loginRequestDto.getPassword())) {
            return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");
        }

        String accessToken = jwtTokenUtils.generateJwtToken(member);
        tokenToHeaders(accessToken, response);

        return ResponseDto.success(
                KakaoUserDto.builder()
                        .id(member.getMemberId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .build()
        );
    }


    @Transactional(readOnly = true)
    public Member isPresentMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(String token, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + token);
    }

    public ResponseDto<?> logout(HttpServletRequest request) {
        request.removeAttribute("Authorization");
        return ResponseDto.success("로그아웃되었습니다.");
    }
}
