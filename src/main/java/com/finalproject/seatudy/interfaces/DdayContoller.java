package com.finalproject.seatudy.interfaces;

import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.DdayService;
import com.finalproject.seatudy.service.dto.request.DdayRequestDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class DdayContoller {

    private final DdayService ddayService;

    @PostMapping("/ddays")
    @ApiOperation(value = "D-day 생성")
    public ResponseDto<?> createDday(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                     @RequestBody DdayRequestDto requestDto) throws ParseException {
        return ddayService.createDday(userDetails, requestDto);
    }

    @GetMapping("/ddays")
    @ApiOperation(value = "D-day 조회")
    public ResponseDto<?> getDday(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException {
        return ddayService.getDday(userDetails);
    }

    @GetMapping("/ddays/dates")
    @ApiOperation(value = "D-day 날짜별 조회")
    @ApiImplicitParam(name = "selectDate", value = "날짜 선택")
    public ResponseDto<?> getDdayByDates(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestParam String selectDate) throws ParseException {
        return ddayService.getDdayByDates(userDetails, selectDate);
    }


    @PutMapping("/ddays/{ddayId}")
    @ApiOperation(value = "D-day 수정")
    @ApiImplicitParam(name = "ddayId", value = "D-day 아이디")
    public ResponseDto<?> updateDday(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable Long ddayId,
                                      @RequestBody DdayRequestDto requestDto) throws ParseException{
        return ddayService.updateDday(userDetails, ddayId, requestDto);
    }

    @DeleteMapping("/ddays/{ddayId}")
    @ApiOperation(value = "D-day 삭제")
    @ApiImplicitParam(name = "ddayId", value = "D-day 아이디")
    public ResponseDto<?> deleteDday(@AuthenticationPrincipal UserDetailsImpl userDetails,
                            @PathVariable Long ddayId){
        return ddayService.deleteDday(userDetails, ddayId);
    }
}
