package com.finalproject.seatudy.timeCheck.util;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static com.finalproject.seatudy.timeCheck.util.Formatter.sdf;
import static com.finalproject.seatudy.timeCheck.util.Formatter.sdtf;

public class CalendarUtil {

    // 캘린더 생성
    // SimpleDateFormat.parse()메소드는 입력한 문자열 형식의 날짜가 Format이 다를경우 java.text.ParseException을 발생한다.
    public static Calendar todayCalendar(String date) throws ParseException{
        Calendar today = Calendar.getInstance(); // 캘린더를 생성 (캘린더는 추상클래스이므로 getInstance()을 통해 구현된 객체를 얻어야함)
        today.setTime(sdf.parse(date)); // 파라미터 기준으로 캘린더 셋팅
        return today;
    }

    // 날짜 형식에 맞게 string으로 포맷
    public static String dateFormat(Calendar calendar){
        return sdf.format(calendar.getTime());
    }

    // 현재 시간 기준으로 셋팅
    public static void todayCalendarTime(Calendar setTime) throws ParseException{
        String nowDateTime = nowDateTime(); // 현재 시간 -> string yyyy-MM-dd HH:mm:ss 형식
        Date nowFormatter = sdtf.parse(nowDateTime); // DateTime 형식으로 포맷
        setTime.setTime(nowFormatter);
    }

    // 당일 오전 다섯시로 셋팅
    public static void setCalendarTime(Calendar setTime) throws ParseException {
        String strToday = dateFormat(setTime); //오늘 날짜 str yyyy-MM-dd 형식
        Date setFormatter = dateTimeFormat(strToday); //yyyy-MM-dd 05:00:00(당일 오전 5시)
        setTime.setTime(setFormatter); //yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용
    }

    // String yyyy-MM-dd HH:mm:ss 형식으로 return; 현재 시간
    private static String nowDateTime() {
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        String nowYear = String.valueOf(nowSeoul.getYear());
        String nowMonth = String.valueOf(nowSeoul.getMonthValue());
        String nowDay = String.valueOf(nowSeoul.getDayOfMonth());
        String nowTime = nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return nowYear + "-" + nowMonth + "-" + nowDay + " " + nowTime;
    }

    // 전 날 오전 5시 기준 데이터 포맷
    private static Date dateTimeFormat(String setDateTime) throws ParseException {
        String setToday = setDateTime + "05:00:00"; //어제 날짜 + 오전5시 -> 조건을 걸기 위해 만들어줌?
        return sdtf.parse(setToday);
    }

//    public static LocalDate localDate(String date) throws ParseException{
//        LocalDate today = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
//        return today;
//    }
//
//    public static String dateFormat(LocalDate localDate){
//        return sdf.format(localDate.now());
//    }
}
