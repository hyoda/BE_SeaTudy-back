package com.finalproject.seatudy.timeCheck.controller;

import com.finalproject.seatudy.timeCheck.Dto.TimeCheckListDto;
import com.finalproject.seatudy.timeCheck.service.TimeCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TimeCheckController {

    private final TimeCheckService timeCheckService;

    // 체크인 start
    @PostMapping("/checkIns")
    public String checkIn() throws ParseException{
        log.info("요청 메서드 [POST] /api/v1/checkIns");
        return timeCheckService.checkIn();
    }

    // 로그인된 사용자가 이미 start를 누른 상태라면, 값을 내려준다.
    @GetMapping("/checkIns")
    public TimeCheckListDto.TimeCheckDto getCheckIn() throws ParseException{
        log.info("요청 메서드 [GET] /api/checkIn");
        return timeCheckService.getCheckIn();
    }
}
