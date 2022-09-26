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

import static com.finalproject.seatudy.service.util.CalendarUtil.totalTime;
import static com.finalproject.seatudy.service.util.Formatter.sdf;
import static com.finalproject.seatudy.service.util.Formatter.stf;

@Slf4j
@RequiredArgsConstructor
@Service
public class RankService {

    private final RankRepository rankRepository;
    private final WeekRankRepository weekRankRepository;
    private final MemberRepository memberRepository;

    public ResponseDto<?> getDayRank(String date) {
        List<Rank> dayStudyRanks = rankRepository.findTop20ByDateOrderByDayStudyDesc(date);

        return ResponseDto.success(dayStudyRanks.stream().map(RankResponseDto::fromEntity)
                .collect(Collectors.toList()));
    }

    public ResponseDto<?> getWeekDayRank(String date) throws ParseException {

        String strDate = date;
        Date weekDate = sdf.parse(strDate);
        weekDate = new Date(weekDate.getTime() + (1000 * 60 * 60 * 24 - 1));
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(weekDate);

        int week = cal.get(Calendar.WEEK_OF_YEAR);
        List<WeekRank> weekDayStudyRanks = weekRankRepository.findTop20ByWeekOrderByWeekStudyDesc(week);

        return ResponseDto.success(weekDayStudyRanks.stream().map(WeekRankResponseDto::fromEntity)
                .collect(Collectors.toList()));
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

    public ResponseDto<?> getWeekStudy(String date, UserDetailsImpl userDetails) throws ParseException {
        Member member = userDetails.getMember();

        Date weekDate = null;
        String strDate = date;
        weekDate = sdf.parse(strDate);
        weekDate = new Date(weekDate.getTime() + (1000 * 60 * 60 * 24 - 1));
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(weekDate);

        int week = cal.get(Calendar.WEEK_OF_YEAR);

        Optional<WeekRank> weekStudy = weekRankRepository.findByMemberAndWeek(member, week);
        String[] arrayFind = weekStudy.get().getWeekStudy().split(":");
        int weekHH = Integer.parseInt(arrayFind[0]);
        WeekStudyResponseDto responseDto = new WeekStudyResponseDto(week, weekHH);

        return ResponseDto.success(responseDto);
    }

    public ResponseDto<?> getWeekStudyDetail(String date, UserDetailsImpl userDetails) throws ParseException {

        Member member = userDetails.getMember();

        String strDate = date;
        Date weekDate = sdf.parse(strDate);
        weekDate = new Date(weekDate.getTime() + (1000 * 60 * 60 * 24 - 1));
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(weekDate);

        int week = cal.get(Calendar.WEEK_OF_YEAR);
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
        Date date = null;
        String strDate = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜
        date = sdf.parse(strDate);
        date = new Date(date.getTime() + (1000 * 60 * 60 * 24 - 1));
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);

        int week = cal.get(Calendar.WEEK_OF_YEAR);

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
                    .member(member)
                    .build();
            weekRankRepository.save(afterWeekRank);
        }

//        List<WeekRank> weekRanks = weekRankRepository.findAll();
//        return weekRanks;
    }

}
