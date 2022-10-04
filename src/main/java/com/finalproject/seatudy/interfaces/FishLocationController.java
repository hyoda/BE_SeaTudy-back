package com.finalproject.seatudy.interfaces;

import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.FishLocationService;
import com.finalproject.seatudy.service.dto.request.FishLocationReqDto;
import com.finalproject.seatudy.service.dto.response.FishLocationResDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class FishLocationController {

    private final FishLocationService fishLocationService;

    @GetMapping("/fishes/locations")
    public ResponseDto<?> getFishLocations(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return fishLocationService.getFishLocations(userDetails);
    }

    @PutMapping("/fishes/relocations")
    public ResponseDto<?> updateFishLocation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestBody FishLocationReqDto fishLocationReqDto) {
        return fishLocationService.updateFishLocation(userDetails, fishLocationReqDto);
    }

    @PutMapping("/fishes/relocations/{fishNum}")
    public ResponseDto<?> resetFishLocation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable Long fishNum) {
        return fishLocationService.resetFishLocation(userDetails, fishNum);
    }

    @PutMapping("/fishes/allRelocations")
    public ResponseDto<?> resetAllFishLocations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return fishLocationService.resetAllFishLocations(userDetails);
    }
}
