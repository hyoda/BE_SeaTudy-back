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

    @PostMapping("/fishes/locations")
    public ResponseDto<?> createFishLocation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestBody FishLocationReqDto fishLocationReqDto) {
        return fishLocationService.createFishLocation(userDetails, fishLocationReqDto);
    }

    @GetMapping("/fishes/locations")
    public ResponseDto<?> getFishLocations(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return fishLocationService.getFishLocations(userDetails);
    }

    @PutMapping("/fishes/relocations")
    public ResponseDto<?> updateFishLocation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestBody FishLocationReqDto fishLocationReqDto) {
        return fishLocationService.updateFishLocation(userDetails, fishLocationReqDto);
    }
}
