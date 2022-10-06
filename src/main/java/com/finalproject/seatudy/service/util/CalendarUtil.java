package com.finalproject.seatudy.service.util;

import com.finalproject.seatudy.domain.entity.Rank;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.finalproject.seatudy.service.util.Formatter.sdf;
import static com.finalproject.seatudy.service.util.Formatter.sdtf;
import static java.lang.String.format;

public class CalendarUtil {

    public static Calendar todayCalendar(String date) throws ParseException{
        Calendar today = Calendar.getInstance();
        today.setTime(sdf.parse(date));
        return today;
    }

    public static String dateFormat(Calendar calendar){
        return sdf.format(calendar.getTime());
    }

    public static void todayCalendarTime(Calendar setTime) throws ParseException{
        String nowDateTime = nowDateTime();
        Date nowFormatter = sdtf.parse(nowDateTime);
        setTime.setTime(nowFormatter);
    }

    public static void setCalendarTime(Calendar setTime) throws ParseException {
        String strToday = dateFormat(setTime);
        Date setFormatter = dateTimeFormat(strToday);
        setTime.setTime(setFormatter);
    }

    public static String totalTime(List<Rank> allUserList) {

        int total = 0; //총 누적 공부시간이 담김

        for (Rank find : allUserList) {
            String[] arrayFind = find.getDayStudy().split(":");
            int allHH = Integer.parseInt(arrayFind[0]);
            int allMM = Integer.parseInt(arrayFind[1]);
            int allSS = allHH * 3600 + allMM * 60;
            int alltotal = allSS + Integer.parseInt(arrayFind[2]); //초으로 환산
            total += alltotal;
        }

        int totalHH = (total / 3600);
        int totalMM = ((total % 3600) / 60);
        int totalSS = ((total % 3600) % 60);

        return format("%02d:%02d:%02d",totalHH,totalMM,totalSS);
    }

    public static int totalPoint(List<Rank> allUserList) {
        long total = 0L; //총 누적 공부시간이 담김

        for (Rank find : allUserList) {
            String[] arrayFind = find.getDayStudy().split(":");
            int allHH = Integer.parseInt(arrayFind[0]);
            int allMM = Integer.parseInt(arrayFind[1]);
            int allSS = allHH * 3600 + allMM * 60;
            int alltotal = allSS + Integer.parseInt(arrayFind[2]); //초으로 환산
            total += alltotal;
        }
            int totalHH = (int)(total / 3600);

        return totalHH;
    }


    private static String nowDateTime() {
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        String nowYear = String.valueOf(nowSeoul.getYear());
        String nowMonth = String.valueOf(nowSeoul.getMonthValue());
        String nowDay = String.valueOf(nowSeoul.getDayOfMonth());
        String nowTime = nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return nowYear + "-" + nowMonth + "-" + nowDay + " " + nowTime;
    }

    private static Date dateTimeFormat(String setDate) throws ParseException {
        String setToday = setDate + "05:00:00";
        return sdtf.parse(setToday);
    }


    public static Calendar getToday() throws ParseException {
        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar setDay = todayCalendar(date);
        setCalendarTime(setDay);

        Calendar today = todayCalendar(date);
        todayCalendarTime(today);

        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1);
        }
        return today;
    }
}
