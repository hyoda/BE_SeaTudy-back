package com.finalproject.seatudy.interfaces;

import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.RankService;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RankController {

    private final RankService rankService;

    @GetMapping("/dayRanks")
    @ApiOperation(value = "일일 랭킹 조회")
    public ResponseDto<?> getDayRank(@RequestParam String date) {
        return rankService.getDayRank(date);
    }

    @GetMapping("/weekDayRanks")
    @ApiOperation(value = "주간 랭킹 조회")
    public ResponseDto<?> getWeekDayRank(@RequestParam String date) throws ParseException {
        return rankService.getWeekDayRank(date);
    }

    @GetMapping("/dayStudies")
    @ApiOperation(value = "일일 공부량 조회")
    public ResponseDto<?> getAllDayStudyByYear(@RequestParam String year,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return rankService.getAllDayStudyByYear(year, userDetails);
    }

    @GetMapping("/weekStudies")
    @ApiOperation(value = "주간 공부량 조회")
    public ResponseDto<?> getWeekStudy(@RequestParam String date,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException {
        return rankService.getWeekStudy(date, userDetails);
    }

    @GetMapping("/weekStudies/details")
    @ApiOperation(value = "주간 요일별 공부량 리스트 조회")
    public ResponseDto<?> getWeekStudyDetail(@RequestParam String date,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException {

        return rankService.getWeekStudyDetail(date, userDetails);
    }
}
