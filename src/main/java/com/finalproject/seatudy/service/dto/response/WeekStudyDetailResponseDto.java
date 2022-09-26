package com.finalproject.seatudy.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WeekStudyDetailResponseDto {
    private String weekDay;
    private int hour;

    public static WeekStudyDetailResponseDto toDto(String weekDay, int hour) {

        return new WeekStudyDetailResponseDto(
                weekDay,
                hour
        );
    }
}
