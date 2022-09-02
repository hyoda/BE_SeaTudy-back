package com.finalproject.seatudy.timeCheck.service;

import com.finalproject.seatudy.Rank.Rank;
import com.finalproject.seatudy.Rank.RankRepository;
import com.finalproject.seatudy.login.Member;
import com.finalproject.seatudy.login.MemberRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.timeCheck.Dto.TimeCheckListDto;
import com.finalproject.seatudy.timeCheck.entity.TimeCheck;
import com.finalproject.seatudy.timeCheck.repository.TimeCheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static com.finalproject.seatudy.timeCheck.util.CalendarUtil.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TimeCheckService {

    private final TimeCheckRepository timeCheckRepository;
    private final MemberRepository memberRepository;
    private final RankRepository rankRepository;


    @Transactional
    public String checkIn(UserDetailsImpl userDetails) throws ParseException {

        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("NON_EXISTENT_USER")
        );

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar setDay = todayCalendar(date); // 오늘 기준 캘린더
        setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = todayCalendar(date); // 현재 시간 기준 날짜
        todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        // compareTo() < 0 : 현재시간이 캘린더보다 작으면(음수) 과거
        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1);  // 오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = dateFormat(today); //날짜 형식에 맞게 String형태로 포맷

        List<TimeCheck> timeChecks = timeCheckRepository.findByMemberAndDate(member,setToday);
        Optional<Rank> rank = rankRepository.findByMemberAndDate(member, setToday);

        //체크아웃을 하지 않은 상태에서 체크인을 시도할 경우 NPE
        for (TimeCheck timeCheck : timeChecks) {
            if (timeCheck.getCheckOut() == null) {
                throw new RuntimeException("TRY_CHECKOUT");
            }
        }

        String timeResponse = "00:00:00";

        if (rank.isPresent()){
            timeResponse = rank.get().getStudyTime();
        }

        String nowTime = LocalTime.now(ZoneId.of("Asia/Seoul")).toString();
        TimeCheck timeCheck = TimeCheck.builder()
                .member(member)
                .date(setToday)
                .checkIn(nowTime)
                .build();
        timeCheckRepository.save(timeCheck);

        log.info("체크인 {}", timeResponse);

        return timeResponse;
    }

    public TimeCheckListDto.TimeCheckDto getCheckIn(UserDetailsImpl userDetails) throws ParseException {

        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("NON_EXISTENT_USER")
        );

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar setDay = todayCalendar(date); // 오늘 기준 캘린더
        setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = todayCalendar(date); // 현재 시간 기준 날짜
        todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        // compareTo() < 0 : 인자보다 과거
        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1);  // 오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = dateFormat(today);

        Optional<Rank> rank = rankRepository.findByMemberAndDate(member, setToday);

        List<TimeCheck> findCheckIn = timeCheckRepository.findByMemberAndDate(member, setToday);

        List<TimeCheckListDto.TodayLogDto> todayLogDtos = new ArrayList<>(); // 그 날의 로그 기록

        // 기록이 없을 경우
        if (findCheckIn.size() == 0){
            List<Rank> allMemberList = rankRepository.findByMember(member);

            String total = totalTime(allMemberList);

            TimeCheckListDto.TimeCheckDto timeCheckDto = TimeCheckListDto.TimeCheckDto.builder()
                    .daySumTime("00:00:00")
                    .totalSumTime(total)
                    .todayLogs(todayLogDtos)
                    .build();

            log.info("체크인 기록이 없음 {}", timeCheckDto);

            return timeCheckDto;
        }
        return null;
    }
}
