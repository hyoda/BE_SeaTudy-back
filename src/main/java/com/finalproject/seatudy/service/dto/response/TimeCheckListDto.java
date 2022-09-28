package com.finalproject.seatudy.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class TimeCheckListDto {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class CheckIn{
        private String checkIn;
        private String timeWatch;
        private TimeDetail timeDetail;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    public static class CheckOut{
        private String checkOut;
        private String timeWatch;
        private TimeDetail timeDetail;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TimeDetail {
        private int hour;
        private int minute;
        private int second;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TimeCheckDto {
        private String dayStudyTime;
        private String totalStudyTime;
        private Boolean isStudy;
        private List<TodayLogDto> todayLogs;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    public static class TodayLogDto {
        private String checkIn;
        private String checkOut;
    }
}
