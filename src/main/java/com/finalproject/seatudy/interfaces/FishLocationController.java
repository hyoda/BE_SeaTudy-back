package com.finalproject.seatudy.interfaces;

import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.FishLocationService;
import com.finalproject.seatudy.service.dto.request.FishLocationReqDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class FishLocationController {

    private final FishLocationService fishLocationService;

    @GetMapping("/fishes/locations")
    @ApiOperation(value = "물고기 위치 리스트 조회")
    public ResponseDto<?> getFishLocations(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return fishLocationService.getFishLocations(userDetails);
    }

    @PutMapping("/fishes/relocations")
    @ApiOperation(value = "지정한 물고기 위치 변경")
    public ResponseDto<?> updateFishLocation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestBody FishLocationReqDto fishLocationReqDto) {
        return fishLocationService.updateFishLocation(userDetails, fishLocationReqDto);
    }

    @PutMapping("/fishes/relocations/{fishNum}")
    @ApiOperation(value = "지정한 물고기 위치 리셋")
    public ResponseDto<?> resetFishLocation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable Long fishNum) {
        return fishLocationService.resetFishLocation(userDetails, fishNum);
    }

    @PutMapping("/fishes/allRelocations")
    @ApiOperation(value = "전체 물고기 위치 리셋")
    public ResponseDto<?> resetAllFishLocations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return fishLocationService.resetAllFishLocations(userDetails);
    }
}
