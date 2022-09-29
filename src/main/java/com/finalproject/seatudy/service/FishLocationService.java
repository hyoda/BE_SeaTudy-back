package com.finalproject.seatudy.service;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.MemberFish;
import com.finalproject.seatudy.domain.repository.MemberFishRepository;
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

import static com.finalproject.seatudy.security.exception.ErrorCode.FISH_NOT_FOUND;
import static com.finalproject.seatudy.security.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class FishLocationService {

    private final MemberFishRepository memberFishRepository;
    private final MemberRepository memberRepository;

    public ResponseDto<?> getFishLocations(UserDetailsImpl userDetails) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        List<MemberFish> memberFishList =
                memberFishRepository.findAllByMember(member);
        List<FishLocationResDto> fishLocationResDtos = new ArrayList<>();

        for (MemberFish memberFish : memberFishList) {
            fishLocationResDtos.add(FishLocationResDto.fromEntity(memberFish));
        }
        return ResponseDto.success(fishLocationResDtos);
    }

    public ResponseDto<?> updateFishLocation(UserDetailsImpl userDetails, FishLocationReqDto fishLocationReqDto) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        Optional<MemberFish> foundMemberFish =
                memberFishRepository.findByMemberAndFish_FishId(member, fishLocationReqDto.getFishNum()+1);

        if (foundMemberFish.isEmpty()) throw new CustomException(FISH_NOT_FOUND);
        foundMemberFish.get().update(fishLocationReqDto);

        FishLocationResDto resDto = FishLocationResDto.fromEntity(foundMemberFish.get());
        return ResponseDto.success(resDto);
    }

    public ResponseDto<?> resetFishLocation(UserDetailsImpl userDetails, Long fishNum) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        Optional<MemberFish> foundMemberFish =
                memberFishRepository.findByMemberAndFish_FishId(member, fishNum+1);
        if (foundMemberFish.isEmpty()) throw new CustomException(FISH_NOT_FOUND);

        foundMemberFish.get().resetLocation();

        FishLocationResDto resDto = FishLocationResDto.fromEntity(foundMemberFish.get());
        return ResponseDto.success(resDto);
    }
}
