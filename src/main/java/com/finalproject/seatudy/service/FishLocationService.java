package com.finalproject.seatudy.service;

import com.finalproject.seatudy.domain.entity.FishLocation;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.repository.FishLocationRepository;
import com.finalproject.seatudy.domain.repository.MemberRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.security.exception.CustomException;
import com.finalproject.seatudy.service.dto.request.FishLocationReqDto;
import com.finalproject.seatudy.service.dto.response.FishLocationResDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.finalproject.seatudy.security.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class FishLocationService {

    private final FishLocationRepository fishLocationRepository;
    private final MemberRepository memberRepository;

    public ResponseDto<?> createFishLocation(UserDetailsImpl userDetails, FishLocationReqDto fishLocationReqDto) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        List<FishLocation> fishLocations = fishLocationRepository.findAllByMember(member);
        if (fishLocations.size() > 0) {
            for (FishLocation fishLocation : fishLocations) {
                if (fishLocation.getFishNum() == fishLocationReqDto.getFishNum()) {
                    throw new CustomException(DUPLICATE_FISH);
                }
            }
        }
        FishLocation fishLocation = FishLocation.builder()
                .member(member)
                .leftValue(fishLocationReqDto.getLeftValue())
                .topValue(fishLocationReqDto.getTopValue())
                .fishNum(fishLocationReqDto.getFishNum())
                .build();

        fishLocationRepository.save(fishLocation);

        FishLocationResDto fishLocationResDto = FishLocationResDto.fromEntity(fishLocation);

        return ResponseDto.success(fishLocationResDto);
    }


    public ResponseDto<?> getFishLocations(UserDetailsImpl userDetails) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        List<FishLocation> fishLocations = fishLocationRepository.findAllByMember(member);
        List<FishLocationResDto> fishLocationResDtos = new ArrayList<>();

        for (FishLocation fishLocation : fishLocations) {
            fishLocationResDtos.add(FishLocationResDto.fromEntity(fishLocation));
        }
        return ResponseDto.success(fishLocationResDtos);
    }

    public ResponseDto<?> updateFishLocation(UserDetailsImpl userDetails, FishLocationReqDto fishLocationReqDto) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        Optional<FishLocation> foundFishLocation = fishLocationRepository.findByMemberAndFishNum(member, fishLocationReqDto.getFishNum());
        if (foundFishLocation.isEmpty()) {
            throw new CustomException(FISH_NOT_FOUND);
        }
        foundFishLocation.get().update(fishLocationReqDto);

        FishLocationResDto resDto = FishLocationResDto.fromEntity(foundFishLocation.get());

        return ResponseDto.success(resDto);
    }
}
