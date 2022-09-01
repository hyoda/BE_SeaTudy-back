package com.finalproject.seatudy.dday.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DdayRequestDto {

    private Long id;
    private String title;
    private String dDay;

}
