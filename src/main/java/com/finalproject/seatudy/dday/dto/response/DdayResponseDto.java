package com.finalproject.seatudy.dday.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DdayResponseDto {
    private Long id;
    private String title;
    private String dDay;
}
