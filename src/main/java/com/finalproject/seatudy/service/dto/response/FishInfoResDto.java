package com.finalproject.seatudy.service.dto.response;

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
}
