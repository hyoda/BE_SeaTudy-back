package com.finalproject.seatudy.service.dto.response;

import com.finalproject.seatudy.domain.entity.Fish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FishInfoResDto {
    private long fishNum;
    private String fishName;
    private String fishImageUrl;
    private String fishInfo;

    public static FishInfoResDto fromEntity(Fish fish) {
        return new FishInfoResDto(
                fish.getFishId(),
                fish.getFishName(),
                fish.getFishImageUrl(),
                fish.getFishInfo()
        );
    }
}
