package com.finalproject.seatudy.timeCheck.Dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class TimeCheckListDto {

    @Builder
    @Getter
    public static class TimeCheckDto {
        private String daySumTime;
        private String totalSumTime;
        private List<TodayLogDto> todayLogs;
    }
    @Builder
    @Getter
    public static class TodayLogDto {
        private String checkIn;
        private String checkOut;
    }
}
