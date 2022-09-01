package com.finalproject.seatudy.dday.controller;

import com.finalproject.seatudy.dday.dto.request.DdayRequestDto;
import com.finalproject.seatudy.dday.service.DdayService;
import com.finalproject.seatudy.dto.response.ResponseDto;
import org.springframework.web.bind.annotation.*;

@RestController
public class DdayController {

    private final DdayService ddayService;

    public DdayController(DdayService ddayService) {
        this.ddayService = ddayService;
    }

    @GetMapping("/api/v1/ddays")
    public ResponseDto<?> getAllDday(){
        return ddayService.getAllDday();
    }

    @PostMapping("/api/v1/ddays")
    public ResponseDto<?> postDday(@RequestBody DdayRequestDto ddayRequestDto){
        return ddayService.postDday(ddayRequestDto);
    }

    @PutMapping("/api/v1/ddays/{ddayId}")
    public ResponseDto<?> updateDday(@PathVariable Long ddayId, @RequestBody DdayRequestDto ddayRequestDto){
        return ddayService.updateDday(ddayId, ddayRequestDto);
    }

    @DeleteMapping("/api/v1/ddays/{ddayId}")
    public ResponseDto<?> deleteDday(@PathVariable Long ddayId){
        return ddayService.deleteDday(ddayId);
    }
}
