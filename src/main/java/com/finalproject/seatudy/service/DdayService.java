package com.finalproject.seatudy.service;

import com.finalproject.seatudy.domain.entity.Dday;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.repository.DdayRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.security.exception.CustomException;
import com.finalproject.seatudy.service.dto.request.DdayRequestDto;
import com.finalproject.seatudy.service.dto.response.DdayResponseDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.finalproject.seatudy.security.exception.ErrorCode.*;
import static com.finalproject.seatudy.service.util.Formatter.sdf;

@RequiredArgsConstructor
@Service
public class DdayService {

    private final DdayRepository ddayRepository;

    @Transactional
    public ResponseDto<?> createDday(UserDetailsImpl userDetails,
                                     DdayRequestDto requestDto) throws ParseException {
        if (requestDto.getTitle().isEmpty()) {
            throw new CustomException(TITLE_NOT_EMPTY);
        }

        Member member = userDetails.getMember();
        checkDuplicateDday(requestDto, member);

        Long ddayResult = ddayCalculate(requestDto);

        Dday dday = Dday.builder()
                .title(requestDto.getTitle())
                .targetDay(requestDto.getTargetDay())
                .dday(ddayResult)
                .member(member)
                .build();
        ddayRepository.save(dday);

        return ResponseDto.success(DdayResponseDto.fromEntity(dday));
    }

    public ResponseDto<?> getDday(UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        List<Dday> ddayList = ddayRepository.findAllByMember(member);

        return ResponseDto.success(ddayList.stream().map(DdayResponseDto::fromEntity)
                .collect(Collectors.toList()));
    }

    @Transactional
    public ResponseDto<?> updateDday(UserDetailsImpl userDetails,
                                     Long ddayId,
                                     DdayRequestDto requestDto) throws ParseException {

        Dday dday = ddayRepository.findById(ddayId).orElseThrow(
                () -> new CustomException(DDAY_NOT_FOUND)
        );

        Member member = userDetails.getMember();
        checkDuplicateDday(requestDto, member);

        if (!member.getEmail().equals(dday.getMember().getEmail())){
            throw new CustomException(DDAY_FORBIDDEN_UPDATE);
        }

        Long ddayResult = ddayCalculate(requestDto);

        dday.update(
                requestDto.getTitle(),
                requestDto.getTargetDay(),
                ddayResult
        );

        return ResponseDto.success(DdayResponseDto.fromEntity(dday));
    }

    @Transactional
    public ResponseDto<?> deleteDday(UserDetailsImpl userDetails, Long ddayId) {

        Member member = userDetails.getMember();

        Dday dday = ddayRepository.findById(ddayId).orElseThrow(
                () -> new CustomException(DDAY_NOT_FOUND)
        );

        if (!member.getEmail().equals(dday.getMember().getEmail())){
            throw new CustomException(DDAY_FORBIDDEN_DELETE);
        }

        ddayRepository.deleteById(ddayId);

        return ResponseDto.success("삭제되었습니다.");
    }

    private Long ddayCalculate (DdayRequestDto requestDto) throws ParseException {
        String targetDay = requestDto.getTargetDay();
        String today = LocalDate.now(ZoneId.of("Asia/Seoul")).toString();

        Date ddate = sdf.parse(targetDay);
        Date todate = sdf.parse(today);
        long Sec = (ddate.getTime() - todate.getTime()) / 1000; // 초
        return Sec / (24*60*60);
    }

    private void checkDuplicateDday(DdayRequestDto requestDto, Member member) {
        List<Dday> ddayResultList = ddayRepository.findAllByMemberAndTargetDay(member, requestDto.getTargetDay());

        for (Dday dday : ddayResultList) {
            if(requestDto.getTitle().equals(dday.getTitle())) throw new CustomException(DUPLICATE_DDAY);
        }
    }
}
