package com.finalproject.seatudy.service.dto.response;

import com.finalproject.seatudy.domain.entity.MemberFish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FishLocationResDto {
    private Long fishNum;
    private int left;
    private int top;

    public static FishLocationResDto fromEntity(MemberFish memberFish){
        return new FishLocationResDto(
                memberFish.getFish().getFishId()-1,
                memberFish.getLeftValue(),
                memberFish.getTopValue()
        );
    }
}
