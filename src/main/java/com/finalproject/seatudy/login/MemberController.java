package com.finalproject.seatudy.login;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.finalproject.seatudy.dto.request.LoginRequestDto;
import com.finalproject.seatudy.dto.request.MemberRequestDto;
import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.login.kakao.KaKaoMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

  private final MemberService memberService;
  private final KaKaoMemberService kaKaoMemberService;

  @RequestMapping(value = "/api/member/signup", method = RequestMethod.POST)
  public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto memberRequestDto) {
    return memberService.createMember(memberRequestDto);
  }

  @RequestMapping(value = "/api/member/login", method = RequestMethod.POST)
  public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto,
      HttpServletResponse response) {
    return memberService.login(loginRequestDto, response);
  }

  @GetMapping("/user/kakao/callback")
  @ResponseBody
  public ResponseDto<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
    return kaKaoMemberService.kakaoLogin(code, response);
  }

  @RequestMapping(value = "/api/auth/member/logout", method = RequestMethod.POST)
  public ResponseDto<?> logout(HttpServletRequest request) {
    return memberService.logout(request);
  }
}
