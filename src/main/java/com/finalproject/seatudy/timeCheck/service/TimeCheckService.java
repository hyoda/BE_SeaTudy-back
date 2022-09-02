package com.finalproject.seatudy.timeCheck.service;

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

import static com.finalproject.seatudy.timeCheck.util.CalendarUtil.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TimeCheckService {

    private final TimeCheckRepository timeCheckRepository;

    @Transactional
    public String checkIn() throws ParseException {

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar setDay = todayCalendar(date); // 오늘 기준 캘린더
        setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = todayCalendar(date); // 현재 시간 기준 날짜
        todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        // compareTo() < 0 : 현재시간이 캘린더보다 작으면(음수) 과거
        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1);  // 오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = dateFormat(today);

        List<TimeCheck> timeChecks = timeCheckRepository.findByDate(setToday);
        //체크아웃을 하지 않은 상태에서 체크인을 시도할 경우 NPE
        for (TimeCheck timeCheck : timeChecks) {
            if (timeCheck.getCheckOut() == null) {
                throw new RuntimeException("TRY_CHECKOUT");
            }
        }

        String timeResponse = "00:00:00";

        String nowTime = LocalTime.now(ZoneId.of("Asia/Seoul")).toString();
        TimeCheck timeCheck = TimeCheck.builder()
                .date(setToday)
                .checkIn(nowTime)
                .build();
        timeCheckRepository.save(timeCheck);

        log.info("체크인 {}", timeResponse);

        return timeResponse;
    }

    public TimeCheckListDto.TimeCheckDto getCheckIn() throws ParseException {

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

        List<TimeCheck> findCheckIn = timeCheckRepository.findByDate(setToday);

        List<TimeCheckListDto.TodayLogDto> todayLogDtos = new ArrayList<>(); // 그 날의 로그 기록

        if (findCheckIn.size() == 0){

        }

    }
}
