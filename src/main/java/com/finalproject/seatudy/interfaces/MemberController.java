package com.finalproject.seatudy.interfaces;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.finalproject.seatudy.service.MemberService;
import com.finalproject.seatudy.service.dto.request.LoginRequestDto;
import com.finalproject.seatudy.service.dto.request.MemberRequestDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import com.finalproject.seatudy.infra.GoogleMemberService;
import com.finalproject.seatudy.infra.KaKaoMemberService;
import com.finalproject.seatudy.infra.NaverMemberService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {

  private final KaKaoMemberService kaKaoMemberService;
  private final NaverMemberService naverMemberService;
  private final GoogleMemberService googleMemberService;

  //카카오 로그인
  @GetMapping("/members/kakaoLogin")
  @ApiOperation(value = "카카오 로그인")
  @ApiImplicitParam(name = "code", value = "카카오 코드")
  public ResponseDto<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
    return kaKaoMemberService.kakaoLogin(code, response);
  }

  //네이버 로그인
  @GetMapping("/members/naverLogin")
  @ApiOperation(value = "네이버 로그인")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "code", value = "네이버 코드"),
          @ApiImplicitParam(name = "state", value = "네이버 상태")
  })
  public ResponseDto<?> naverLogin(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws JsonProcessingException {
    return naverMemberService.naverLogin(code, state, response);
  }

  //구글 로그인
  @GetMapping("/members/googleLogin")
  @ApiOperation(value = "구글 로그인")
  @ApiImplicitParam(name = "code", value = "구글 코드")
  public ResponseDto<?> googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
    return googleMemberService.googleLogin(code, response);
  }
}
