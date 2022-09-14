package com.finalproject.seatudy.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class TimeCheckListDto {
    @Builder
    @Getter
    public static class CheckIn{
        private String checkIn;
        private String timeWatch;
        private int HH;
        private int mm;
        private int ss;
    }
    @Builder
    @Getter
    public static class CheckOut{
        private String checkOut;
        private String timeWatch;
        private int HH;
        private int mm;
        private int ss;
//        private TodayLogDto todayLogDto;
    }

    @Builder
    @Getter
    public static class TimeCheckDto {
        private String dayStudyTime;
        private String totalStudyTime;
        private Boolean isStudy;
        private List<TodayLogDto> todayLogs;
    }
    @Builder
    @Getter
    public static class TodayLogDto {
        private String checkIn;
        private String checkOut;
    }
}
