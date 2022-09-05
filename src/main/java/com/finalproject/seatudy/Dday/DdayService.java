package com.finalproject.seatudy.Dday;

import com.finalproject.seatudy.login.Member;
import com.finalproject.seatudy.login.MemberRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.finalproject.seatudy.timeCheck.util.Formatter.sdf;

@RequiredArgsConstructor
@Service
public class DdayService {

    private final DdayRepository ddayRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public DdayResponseDto createDday(UserDetailsImpl userDetails
                         , DdayRequestDto requestDto) throws ParseException {

        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("NON_EXISTENT_USER")
        );

        String targetDay = requestDto.getTargetDay();
        String today = LocalDate.now(ZoneId.of("Asia/Seoul")).toString();
//        String today = sdf.format(LocalDate.now(ZoneId.of("Asia/Seoul"))); -> localdate를 date형식으로 바꿔져야함

//        Date ddate = sdf.parse(requestDto.getTargetDay());
        Date ddate = sdf.parse(targetDay);
        Date todate = sdf.parse(today);
        long Sec = (ddate.getTime() - todate.getTime()) / 1000; // 초
//        long Min = (ddate.getTime() - todate.getTime()) / 60000; // 분
//        long Hour = (ddate.getTime() - todate.getTime()) / 3600000; // 시
        Long Days = (Sec / (24*60*60)); // 일자수

        Dday dday = Dday.builder()
                .title(requestDto.getTitle())
                .targetDay(requestDto.getTargetDay())
                .dday(Days)
                .member(member)
                .build();
        ddayRepository.save(dday);

        DdayResponseDto responseDto = DdayResponseDto.fromEntity(dday);
        return responseDto;
    }

    public List<DdayResponseDto> getDday(UserDetailsImpl userDetails) {

        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("NON_EXISTENT_USER")
        );

        List<Dday> ddayList = ddayRepository.findAllByMember(member);

        return ddayList.stream().map(DdayResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
