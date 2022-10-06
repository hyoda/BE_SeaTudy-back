package com.finalproject.seatudy.service.dto.response;

import com.finalproject.seatudy.domain.entity.Dday;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DdayResponseDto {
    private Long dDayId;
    private String title;
    private String targetDay;
    private Long dDay;

    public static DdayResponseDto fromEntity(Dday dday){

        return new DdayResponseDto(
                dday.getDdayId(),
                dday.getTitle(),
                dday.getTargetDay(),
                dday.getDday()
        );
    }
}
