package com.finalproject.seatudy.timeCheck.util;

import java.text.SimpleDateFormat;

public class Formatter {
    // SimpleDateFormat는 날짜형식을 지정하는 메소드
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

    public static SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");

    public static SimpleDateFormat time = new SimpleDateFormat("HHmmss");
}
