package com.finalproject.seatudy.service;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.Rank;
import com.finalproject.seatudy.domain.entity.TimeCheck;
import com.finalproject.seatudy.domain.repository.RankRepository;
import com.finalproject.seatudy.domain.repository.TimeCheckRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.security.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.finalproject.seatudy.security.exception.ErrorCode.CHECKIN_NOT_TRY;
import static com.finalproject.seatudy.security.exception.ErrorCode.CHECKOUT_NOT_TRY;
import static com.finalproject.seatudy.service.dto.response.TimeCheckListDto.*;
import static com.finalproject.seatudy.service.util.CalendarUtil.*;
import static com.finalproject.seatudy.service.util.Formatter.sdtf;
import static com.finalproject.seatudy.service.util.Formatter.stf;

@Slf4j
@RequiredArgsConstructor
@Service
public class TimeCheckService {

    private final TimeCheckRepository timeCheckRepository;
    private final RankRepository rankRepository;
    private final RankService rankService;

    @Transactional
    public CheckIn checkIn(UserDetailsImpl userDetails) throws ParseException {

        Member member = userDetails.getMember();

        Calendar today = getToday();
        String setToday = dateFormat(today);

        List<TimeCheck> timeChecks = timeCheckRepository.findByMemberAndDate(member,setToday);
        Optional<Rank> rank = rankRepository.findByMemberAndDate(member, setToday);

        for (TimeCheck timeCheck : timeChecks) {
            if (timeCheck.getCheckOut() == null) {
                throw new CustomException(CHECKOUT_NOT_TRY);
            }
        }

        String timeWatch = "00:00:00";

        if (rank.isPresent()){
            timeWatch = rank.get().getDayStudy();
        }

        String nowTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        TimeCheck timeCheck = TimeCheck.builder()
                .member(member)
                .date(setToday)
                .checkIn(nowTime)
                .build();
        timeCheckRepository.save(timeCheck);

        log.info("체크인 {}", timeWatch);
        TimeDetail timeDetail = getTimeDetail(timeWatch);
        return new CheckIn(nowTime, timeWatch, timeDetail);
    }

    public TimeCheckDto getCheckIn(UserDetailsImpl userDetails) throws ParseException {
        Member member = userDetails.getMember();

        Calendar today = getToday();
        String setToday = dateFormat(today);

        Optional<Rank> rank = rankRepository.findByMemberAndDate(member, setToday);
        List<TimeCheck> findCheckIn = timeCheckRepository.findByMemberAndDate(member, setToday);
        List<TodayLogDto> todayLogDtos = new ArrayList<>();

        if (findCheckIn.size() == 0){
            List<Rank> allMemberList = rankRepository.findAllByMember(member);
            String total = totalTime(allMemberList);

            TimeCheckDto timeCheckDto = new TimeCheckDto("00:00:00", total, false, todayLogDtos);
            log.info("체크인 기록이 없음 {}", member.getEmail());
            return timeCheckDto;
        }

        TimeCheck firstCheckIn = findCheckIn.get(findCheckIn.size()-1);

        Calendar checkInCalendar = Calendar.getInstance();
        String setCheckIn = firstCheckIn.getDate() + " " + firstCheckIn.getCheckIn();
        checkInCalendar.setTime(sdtf.parse(setCheckIn));

        TimeDetail timeDetail = getTimeDetail(firstCheckIn.getCheckIn());
        today.add(Calendar.HOUR, -timeDetail.getHour());
        today.add(Calendar.MINUTE, -timeDetail.getMinute());
        today.add(Calendar.SECOND, -timeDetail.getSecond());

        String dayStudyTime = RankCheck(rank, today);

        List<Rank> allMemberList = rankRepository.findAllByMember(member);

        String total = totalTime(allMemberList);
        for (TimeCheck check : findCheckIn){
            TodayLogDto todayLogDto =  new TodayLogDto(check.getCheckIn(), check.getCheckOut());
            todayLogDtos.add(todayLogDto);
        }

        if (findCheckIn.get(findCheckIn.size() - 1).getCheckOut() != null){
            String todayStudy = rank.get().getDayStudy();
            TimeCheckDto timeCheckDto = new TimeCheckDto(todayStudy, total, false, todayLogDtos);

            log.info("체크인 기록이 1회 이상 있음(timer stop) {}", member.getEmail());
            return timeCheckDto;
        }

        TimeCheckDto timeCheckDto = new TimeCheckDto(dayStudyTime, total, true, todayLogDtos);

        log.info("체크인 기록이 1회 이상 있음(timer continue) {}", member.getEmail());
        return timeCheckDto;
    }

    @Transactional
    public CheckOut checkOut(UserDetailsImpl userDetails) throws ParseException {

        Member member = userDetails.getMember();

        Calendar today = getToday();
        String setToday = dateFormat(today);

        List<TimeCheck> findCheckIns = timeCheckRepository.findByMemberAndDate(member, setToday);
        TimeCheck lastCheckIn = findCheckIns.get(findCheckIns.size() - 1);

        Optional<Rank> findRank = rankRepository.findByMemberAndDate(member, setToday);

        String nowTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String dayStudy = getCheckIn(userDetails).getDayStudyTime();

        List<Rank> allMemberList = rankRepository.findAllByMember(member);
        String total = totalTime(allMemberList);


        if (lastCheckIn.getCheckOut() != null){
            throw new CustomException(CHECKIN_NOT_TRY);
        }

        if (rankRepository.findAllByMember(member).size() == 0) {
            total = dayStudy;
        }

        if (allMemberList.size() != 0 && findRank.isEmpty()) {
            lastCheckIn.setCheckOut(nowTime);
            lastCheckIn.setRank(findCheckIns.get(0).getRank());

            LocalTime dayStudyTime = LocalTime.parse(dayStudy);
            int hh = dayStudyTime.getHour();
            int mm = dayStudyTime.getMinute();
            int ss = dayStudyTime.getSecond();
            int dayStudySecond =  hh * 3600 + mm * 60 + ss;

            TimeDetail timeDetail = getTimeDetail(total);
            int totalSecond = timeDetail.getHour() * 3600
                    + timeDetail.getMinute() * 60 + timeDetail.getSecond();

            int totalTime = dayStudySecond + totalSecond;

            int second = ((totalTime % 3600) % 60);
            int minute = ((totalTime % 3600) / 60);
            int hour = (totalTime / 3600);

            total = String.format("%02d:%02d:%02d",hour,minute,second);
        }

        if (findRank.isPresent()) {
            lastCheckIn.setCheckOut(nowTime);
            lastCheckIn.setRank(findCheckIns.get(0).getRank());
            findRank.get().setDayStudy(dayStudy);
            total = totalTime(allMemberList);
            findRank.get().setTotalStudy(total);

            log.info("체크아웃 {}", total);
            TimeDetail timeDetail = getTimeDetail(dayStudy);
            return new CheckOut(nowTime, dayStudy, timeDetail);
        }

        Calendar cal = rankService.setWeekDate(setToday);

        int week = cal.get(Calendar.WEEK_OF_YEAR)-1;
        if (week == 0){
            week = 53;
        }

        lastCheckIn.setCheckOut(nowTime);
        Rank rank = Rank.builder()
                .dayStudy(dayStudy)
                .totalStudy(total)
                .date(setToday)
                .week(week)
                .member(member)
                .timeChecks(findCheckIns)
                .build();
        lastCheckIn.setRank(rank);
        rankRepository.save(rank);

        TimeDetail timeDetail = getTimeDetail(dayStudy);

        return new CheckOut(nowTime, dayStudy, timeDetail);
    }

    private String RankCheck(Optional<Rank> rank, Calendar today) throws ParseException{
        if (rank.isPresent()){
            String dayStudy = stf.format(today.getTime());
            Calendar rankDay = todayCalendar(rank.get().getDate());
            String setTime = rank.get().getDate() + " " + rank.get().getDayStudy();
            Date setFormatter = sdtf.parse(setTime);
            rankDay.setTime(setFormatter);

            TimeDetail timeDetail = getTimeDetail(dayStudy);
            rankDay.add(Calendar.HOUR, timeDetail.getHour());
            rankDay.add(Calendar.MINUTE, timeDetail.getMinute());
            rankDay.add(Calendar.SECOND, timeDetail.getSecond());

            dayStudy = stf.format(rankDay.getTime());
            return dayStudy;
        }
        return stf.format(today.getTime());
    }


    private TimeDetail getTimeDetail(String time) {
        String[] timeStamp = time.split(":"); //시, 분, 초 나누기

        int HH = Integer.parseInt(timeStamp[0]); //시
        int mm = Integer.parseInt(timeStamp[1]); //분
        int ss = Integer.parseInt(timeStamp[2]); //초

        TimeDetail timeDetail = new TimeDetail(HH, mm, ss);

        return timeDetail;
    }

}
