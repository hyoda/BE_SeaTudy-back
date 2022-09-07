package com.finalproject.seatudy.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DdayResponseDto {
    private Long DdayId;
    private String title;
    private String TargetDay;
    private Long Dday;

    public static DdayResponseDto fromEntity(com.finalproject.seatudy.domain.entity.Dday dday){

        return new DdayResponseDto(
                dday.getDdayId(),
                dday.getTitle(),
                dday.getTargetDay(),
                dday.getDday()
        );
    }
}
