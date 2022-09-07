package com.finalproject.seatudy.Dday;

import com.finalproject.seatudy.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class DdayContoller {

    private final DdayService ddayService;

    @PostMapping("/ddays")
    public DdayResponseDto createDday(@AuthenticationPrincipal UserDetailsImpl userDetails,
                           @RequestBody DdayRequestDto requestDto) throws ParseException {
        return ddayService.createDday(userDetails, requestDto);
    }

    @GetMapping("/ddays")
    public List<DdayResponseDto> getDday(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ddayService.getDday(userDetails);
    }

    @PutMapping("/ddays/{ddayId}")
    public DdayResponseDto updateDday(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable Long ddayId,
                                      @RequestBody DdayRequestDto requestDto) throws ParseException{
        return ddayService.updateDday(userDetails, ddayId, requestDto);
    }

    @DeleteMapping("/ddays/{ddayId}")
    public String deleteDday(@AuthenticationPrincipal UserDetailsImpl userDetails,
                            @PathVariable Long ddayId){
        return ddayService.deleteDday(userDetails, ddayId);
    }
}
