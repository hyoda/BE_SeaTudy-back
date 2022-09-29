package com.finalproject.seatudy.service.dto.response;

import com.finalproject.seatudy.domain.entity.FishLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FishLocationResDto {
    private int fishNum;
    private int leftValue;
    private int topValue;

    public static FishLocationResDto fromEntity(FishLocation fishLocation){
        return new FishLocationResDto(
                fishLocation.getFishNum(),
                fishLocation.getLeftValue(),
                fishLocation.getTopValue()
        );
    }
}
