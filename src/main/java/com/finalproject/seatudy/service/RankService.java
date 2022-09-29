package com.finalproject.seatudy.service;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.Rank;
import com.finalproject.seatudy.domain.entity.WeekRank;
import com.finalproject.seatudy.domain.repository.MemberRepository;
import com.finalproject.seatudy.domain.repository.RankRepository;
import com.finalproject.seatudy.domain.repository.WeekRankRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.finalproject.seatudy.service.util.CalendarUtil.*;
import static com.finalproject.seatudy.service.util.Formatter.sdf;
import static com.finalproject.seatudy.service.util.Formatter.stf;

@Slf4j
@RequiredArgsConstructor
@Service
public class RankService {

    private final RankRepository rankRepository;
    private final WeekRankRepository weekRankRepository;
    private final MemberRepository memberRepository;

    public ResponseDto<?> getDayRank() throws ParseException {

        Calendar today = getToday();
        today.add(Calendar.DATE, -1);
        String setToday = dateFormat(today);

        List<Rank> dayStudyRanks = rankRepository.findTop20ByDateOrderByDayStudyDesc(setToday);

        return ResponseDto.success(dayStudyRanks.stream().map(RankResponseDto::fromEntity)
                .collect(Collectors.toList()));
    }

    public ResponseDto<?> getMyDayRank(UserDetailsImpl userDetails) throws ParseException {
        Member member = userDetails.getMember();

        Calendar today = getToday();
        today.add(Calendar.DATE, -1);
        String setToday = dateFormat(today);

        List<Rank> dayStudyRanks = rankRepository.findTop20ByDateOrderByDayStudyDesc(setToday);
        int myRank = 0;
        for (int i=0; i<dayStudyRanks.size(); i++)
            if(Objects.equals(dayStudyRanks.get(i).getMember().getNickname(), member.getNickname())) {
                Rank rank = dayStudyRanks.get(i);
                if (dayStudyRanks.contains(rank)) {
                    myRank = dayStudyRanks.indexOf(rank)+1;
                }
            }
        MyRankResponseDto responseDto = new MyRankResponseDto(member.getNickname(), myRank);

        return ResponseDto.success(responseDto);
    }

    public ResponseDto<?> getWeekDayRank() throws ParseException {

        Calendar today = getToday();
        String setToday = dateFormat(today);

        Calendar cal = setWeekDate(setToday);

        int year = cal.get(Calendar.YEAR);
        int week = cal.get(Calendar.WEEK_OF_YEAR)-2;
        if (week <= 0) {
            year -= 1;
            week = 52;
        }

        List<WeekRank> weekDayStudyRanks = weekRankRepository.findTop20ByYearAndWeekOrderByWeekStudyDesc(year,week);

        return ResponseDto.success(weekDayStudyRanks.stream().map(WeekRankResponseDto::fromEntity)
                .collect(Collectors.toList()));
    }

    public ResponseDto<?> getMyWeekDayRank(UserDetailsImpl userDetails) throws ParseException {
        Member member = userDetails.getMember();

        Calendar today = getToday();
        String setToday = dateFormat(today);

        Calendar cal = setWeekDate(setToday);

        int year = cal.get(Calendar.YEAR);
        int week = cal.get(Calendar.WEEK_OF_YEAR)-2;
        if (week <= 0) {
            year -= 1;
            week = 52;
        }

        List<WeekRank> weekDayStudyRanks = weekRankRepository.findTop20ByYearAndWeekOrderByWeekStudyDesc(year,week);
        int myRank = 0;
        for (int i=0; i<weekDayStudyRanks.size(); i++)
            if(Objects.equals(weekDayStudyRanks.get(i).getMember().getNickname(), member.getNickname())) {
                    WeekRank weekRank = weekDayStudyRanks.get(i);
                if (weekDayStudyRanks.contains(weekRank)) {
                    myRank = weekDayStudyRanks.indexOf(weekRank)+1;
                }
            }
        MyRankResponseDto responseDto = new MyRankResponseDto(member.getNickname(), myRank);

        return ResponseDto.success(responseDto);
    }

    public ResponseDto<?> getAllDayStudyByYear(String year, UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        List<Rank> dayStudies = rankRepository.findAllByMemberAndDateContaining(member, year);
        List<DayStudyResponseDto> responseDtos = new ArrayList<>();
        for (Rank dayStudy : dayStudies) {
            String[] arrayFind = dayStudy.getDayStudy().split(":");
            int HH = Integer.parseInt(arrayFind[0]);
            responseDtos.add(new DayStudyResponseDto(dayStudy.getDate(), HH));
        }

        return ResponseDto.success(responseDtos);
    }

    public ResponseDto<?> getAllWeekStudy(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();

        List<WeekStudyResponseDto> responseDtos = new ArrayList<>();
        List<WeekRank> weekStudies = weekRankRepository.findTop4ByMemberOrderByWeekDesc(member);
        for (WeekRank weekStudy : weekStudies) {
            String[] arrayFind = weekStudy.getWeekStudy().split(":");
            int weekHH = Integer.parseInt(arrayFind[0]);
            int week = weekStudy.getWeek();
            responseDtos.add(new WeekStudyResponseDto(week, weekHH));
        }


        return ResponseDto.success(responseDtos);
    }

    public ResponseDto<?> getWeekStudyDetail(String date, UserDetailsImpl userDetails) throws ParseException {

        Member member = userDetails.getMember();

        Calendar cal = setWeekDate(date);

        int week = cal.get(Calendar.WEEK_OF_YEAR)-1;
        String[] weekDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};


        List<Rank> weekStudyDetails = rankRepository.findAllByMemberAndWeek(member, week);
        List<WeekStudyDetailResponseDto> responseDtos = new ArrayList<>();
        for (Rank weekStudyDetail : weekStudyDetails) {
            Calendar cal2 = Calendar.getInstance();
            String studyDay = weekStudyDetail.getDate();
            Date dayDate = sdf.parse(studyDay);
            cal2.setTime(dayDate);
            int num = cal2.get(Calendar.DAY_OF_WEEK)-1;
            String weekDay = weekDays[num];

            String[] arrayFind = weekStudyDetail.getDayStudy().split(":");
            int HH = Integer.parseInt(arrayFind[0]);
            responseDtos.add(WeekStudyDetailResponseDto.toDto(weekDay, HH));
        }

        return ResponseDto.success(responseDtos);

    }

    @Scheduled(cron = " 50 59 4 * * 1 ")
    public void weekStudy() throws ParseException {
        log.info("일주일 공부시간 저장 시작");
        String strDate = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜
        Calendar cal = setWeekDate(strDate);

        int year = cal.get(Calendar.YEAR);
        int week = cal.get(Calendar.WEEK_OF_YEAR) - 1;

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            List<Rank> ranks = rankRepository.findAllByMember(member);
            if (ranks.size() == 0) {
                continue;
            }
            String weekStudy = ranks.get(ranks.size() - 1).getTotalStudy();

            List<WeekRank> weekRanks = weekRankRepository.findAllByMember(member);
            if (weekRanks.size() == 0) {
                WeekRank firstWeekRank = WeekRank.builder()
                        .weekStudy(weekStudy)
                        .totalStudy(weekStudy)
                        .week(week)
                        .year(year)
                        .member(member)
                        .build();
                weekRankRepository.save(firstWeekRank);
                continue;
            }

            List<Rank> allMemberList = rankRepository.findAllByMember(member);

            String totalTime = totalTime(allMemberList);
            Date totalStudyTime = stf.parse(totalTime);

            WeekRank weeksStudy = weekRanks.get(weekRanks.size() - 1);
            String weekTime = weeksStudy.getTotalStudy();
            Date weekStudyTime = stf.parse(weekTime);

            long second = (totalStudyTime.getTime() - weekStudyTime.getTime()) / 1000;
            long minute = second / 60;
            long hour = minute / 60;

            String weekStudyFormat = String.format("%02d:%02d:%02d", hour, minute, second);
            WeekRank afterWeekRank = WeekRank.builder()
                    .weekStudy(weekStudyFormat)
                    .totalStudy(totalTime)
                    .week(week)
                    .year(year)
                    .member(member)
                    .build();
            weekRankRepository.save(afterWeekRank);
        }

    }

    public Calendar setWeekDate(String date) throws ParseException {
        String strDate = date;
        Date weekDate = sdf.parse(strDate);
        weekDate = new Date(weekDate.getTime() + (1000 * 60 * 60 * 24 - 1));
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(weekDate);
        return cal;
    }

    private Calendar getToday() throws ParseException {
        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString();

        Calendar setDay = todayCalendar(date); // 오늘 기준 캘린더
        setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = todayCalendar(date); // 현재 시간 기준 날짜
        todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        // compareTo() < 0 : 현재시간이 캘린더보다 작으면(음수) 과거
        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1);  // 오전 5시보다 과거라면, 현재 날짜에서 -1
        }
        return today;
    }

}
